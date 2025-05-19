//package com.cts.service;
//
//import com.cts.dto.ProductViewDTO;
//import com.cts.entity.ProductSubscription;
//import com.cts.entity.Products;
//import com.cts.entity.User;
//import com.cts.enums.UserRole;
//import com.cts.exception.InvalidInputException;
//import com.cts.exception.InvalidProductException;
//import com.cts.exception.InvalidSubscriptionException;
//import com.cts.exception.UserNotFoundException;
//import com.cts.repository.ProductRepository;
//import com.cts.repository.ProductViewRepository;
//import com.cts.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.core.ResponseBytes;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectResponse;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.S3Exception;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyBoolean;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private ProductViewRepository productViewRepo;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private SNSService snsService;
//
//    @Mock
//    private S3Client s3Client;
//
//    @Mock
//    private S3Presigner s3Presigner;
//
//    private ProductServiceImpl productService;
//
//    @BeforeEach
//    void setUp() {
//        productService = new ProductServiceImpl("testAccessKey", "testSecretKey");
//        ReflectionTestUtils.setField(productService, "productRepository", productRepository);
//        ReflectionTestUtils.setField(productService, "productViewRepo", productViewRepo);
//        ReflectionTestUtils.setField(productService, "userRepository", userRepository);
//        ReflectionTestUtils.setField(productService, "snsService", snsService);
//        ReflectionTestUtils.setField(productService, "s3Client", s3Client);
//        ReflectionTestUtils.setField(productService, "s3Presigner", s3Presigner);
//        ReflectionTestUtils.setField(productService, "bucketName", "test-bucket");
//        ReflectionTestUtils.setField(productService, "s3KeyPrefix", "product/");
//    }
//
//    @Test
//    void viewAllProducts_shouldReturnListOfActiveProductViewDTOs() {
//        // Arrange
//        Products product1 = createProduct(1, "Product 1", "Description 1", "url1", true, Collections.emptyList());
//        Products product2 = createProduct(2, "Product 2", "Description 2", "url2", true, Collections.emptyList());
//        ProductViewDTO viewDTO1 = createProductViewDTO(1, "Product 1", "Description 1", "url1", 4.5);
//        ProductViewDTO viewDTO2 = createProductViewDTO(2, "Product 2", "Description 2", "url2", 3.8);
//
//        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
//        when(productViewRepo.getReferenceById(1)).thenReturn(viewDTO1);
//        when(productViewRepo.getReferenceById(2)).thenReturn(viewDTO2);
//        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
//        when(productRepository.findById(2)).thenReturn(Optional.of(product2));
//
//        // Act
//        List<ProductViewDTO> result = productService.viewAllProducts();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("Product 1", result.get(0).getName());
//        assertEquals("Product 2", result.get(1).getName());
//        verify(productRepository, times(1)).findAll();
//        verify(productViewRepo, times(1)).getReferenceById(1);
//        verify(productViewRepo, times(1)).getReferenceById(2);
//        verify(productRepository, times(1)).findById(1);
//        verify(productRepository, times(1)).findById(2);
//    }
//
//    @Test
//    void viewAllProducts_shouldThrowExceptionIfNoActiveProducts() {
//        // Arrange
//        when(productRepository.findAll()).thenReturn(Collections.emptyList());
//
//        // Act and Assert
//        assertThrows(InvalidProductException.class, () -> productService.viewAllProducts());
//        verify(productRepository, times(1)).findAll();
//        verify(productViewRepo, never()).getReferenceById(anyInt());
//        verify(productRepository, never()).findById(anyInt());
//    }
//
//    @Test
//    void viewAllProducts_shouldFilterOutInactiveProducts() {
//        // Arrange
//        Products activeProduct = createProduct(1, "Active Product", "Desc", "url", true, Collections.emptyList());
//        Products inactiveProduct = createProduct(2, "Inactive Product", "Desc", "url", false, Collections.emptyList());
//        ProductViewDTO activeViewDTO = createProductViewDTO(1, "Active Product", "Desc", "url", 4.0);
//
//        when(productRepository.findAll()).thenReturn(Arrays.asList(activeProduct, inactiveProduct));
//        when(productViewRepo.getReferenceById(1)).thenReturn(activeViewDTO);
//        when(productRepository.findById(1)).thenReturn(Optional.of(activeProduct));
//
//        // Act
//        List<ProductViewDTO> result = productService.viewAllProducts();
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals("Active Product", result.get(0).getName());
//        verify(productRepository, times(1)).findAll();
//        verify(productViewRepo, times(1)).getReferenceById(1);
//        verify(productRepository, times(1)).findById(1);
//        verify(productRepository, never()).findById(2);
//    }
//
//    @Test
//    void viewProductById_shouldReturnProductViewDTOIfExists() {
//        // Arrange
//        ProductViewDTO viewDTO = createProductViewDTO(1, "Test Product", "Test Description", "test-url", 4.2);
//        when(productViewRepo.findById(1)).thenReturn(Optional.of(viewDTO));
//
//        // Act
//        ProductViewDTO result = productService.viewProductById(1);
//
//        // Assert
//        assertEquals("Test Product", result.getName());
//        assertEquals(4.2, result.getAvg_rating());
//        verify(productViewRepo, times(1)).findById(1);
//    }
//
//    @Test
//    void viewProductById_shouldThrowNoSuchElementExceptionIfNotFound() {
//        // Arrange
//        when(productViewRepo.findById(1)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(java.util.NoSuchElementException.class, () -> productService.viewProductById(1));
//        verify(productViewRepo, times(1)).findById(1);
//    }
//
//    @Test
//    void addProduct_shouldSaveProductAndUploadImageToS3() throws IOException {
//        // Arrange
//        String name = "New Product";
//        String description = "New Description";
//        MultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "some image".getBytes());
//        Boolean isActive = true;
//        Products savedProduct = createProduct(1, name, description, "https://test-bucket.s3.us-east-1.amazonaws.com/product/New_Product.jpg", isActive, Collections.emptyList());
//
//        when(productRepository.save(any(Products.class))).thenReturn(savedProduct);
//        doNothing().when(s3Client).putObject(any(PutObjectRequest.class), any());
//
//        // Act
//        Products result = productService.addProduct(name, description, imageFile, isActive);
//
//        // Assert
//        assertEquals(name, result.getName());
//        assertTrue(result.getImageUrl().startsWith("https://test-bucket.s3.us-east-1.amazonaws.com/product/New_Product.jpg"));
//        assertTrue(result.getIsActive());
//        verify(productRepository, times(1)).save(any(Products.class));
//        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any());
//    }
//
//    @Test
//    void addProduct_shouldThrowInvalidInputExceptionForMissingName() {
//        // Arrange
//        MultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "some image".getBytes());
//        Boolean isActive = true;
//
//        // Act and Assert
//        assertThrows(InvalidInputException.class, () -> productService.addProduct(null, "Description", imageFile, isActive));
//        assertThrows(InvalidInputException.class, () -> productService.addProduct("", "Description", imageFile, isActive));
//        verify(productRepository, never()).save(any(Products.class));
//        verify(s3Client, never()).putObject(any(PutObjectRequest.class), any());
//    }
//
//    @Test
//    void addProduct_shouldThrowInvalidInputExceptionForMissingIsActive() {
//        // Arrange
//        MultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "some image".getBytes());
//
//        // Act and Assert
//        assertThrows(InvalidInputException.class, () -> productService.addProduct("Name", "Description", imageFile, null));
//        verify(productRepository, never()).save(any(Products.class));
//        verify(s3Client, never()).putObject(any(PutObjectRequest.class), any());
//    }
//
//    @Test
//    void addProduct_shouldThrowInvalidInputExceptionForMissingImageFile() {
//        // Arrange
//        Boolean isActive = true;
//
//        // Act and Assert
//        assertThrows(InvalidInputException.class, () -> productService.addProduct("Name", "Description", null, isActive));
//        verify(productRepository, never()).save(any(Products.class));
//        verify(s3Client, never()).putObject(any(PutObjectRequest.class), any());
//    }
//
//    @Test
//    void addProduct_shouldThrowInvalidInputExceptionForInvalidImageType() {
//        // Arrange
//        MultipartFile imageFile = new MockMultipartFile("image", "image.txt", "text/plain", "some text".getBytes());
//        Boolean isActive = true;
//
//        // Act and Assert
//        assertThrows(InvalidInputException.class, () -> productService.addProduct("Name", "Description", imageFile, isActive));
//        verify(productRepository, never()).save(any(Products.class));
//        verify(s3Client, never()).putObject(any(PutObjectRequest.class), any());
//    }
//
//    @Test
//    void updateProduct_shouldUpdateProductDetailsAndCallS3Upload() throws IOException {
//        // Arrange
//        String existingName = "Old Product";
//        String newName = "Updated Product";
//        String newDescription = "Updated Description";
//        MultipartFile newImageFile = new MockMultipartFile("newImage", "new_image.png", "image/png", "new image data".getBytes());
//        Boolean newIsActive = false;
//
//        User user1 = createUser(101, "user1@example.com", "user1", UserRole.USER);
//        User admin1 = createUser(102, "admin1@example.com", "admin1", UserRole.ADMIN);
//        Products existingProduct = createProduct(1, existingName, "Old Description", "old-url", true, Arrays.asList(
//                createSubscription(1, user1, true),
//                createSubscription(2, admin1, true)
//        ));
//        Products updatedProduct = createProduct(1, newName, newDescription, "https://test-bucket.s3.us-east-1.amazonaws.com/product/Updated_Product.png", newIsActive, existingProduct.getProductSubscriptionList());
//
//        when(productRepository.findByName(existingName)).thenReturn(Optional.of(existingProduct));
//        when(productRepository.save(any(Products.class))).thenReturn(updatedProduct);
//        doNothing().when(snsService).notifyAdminOnUpdateProduct();
//        doNothing().when(snsService).notifyUserOnUpdateProduct(List.of("user1@example.com"));
//
//        // Act
//        Products result = productService.updateProduct(existingName, newName, newDescription, newImageFile, newIsActive);
//
//        // Assert
//        assertEquals(newName, result.getName());
//        assertEquals(newDescription, result.getDescription());
//        assertEquals(newIsActive, result.getIsActive());
//        assertTrue(result.getImageUrl().startsWith("https://test-bucket.s3.us-east-1.amazonaws.com/product/Updated_Product.png"));
//        verify(productRepository, times(1)).findByName(existingName);
//        verify(productRepository, times(1)).save(any(Products.class));
//        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any()); // Verify putObject is called
//        verify(snsService, times(1)).notifyAdminOnUpdateProduct();
//        verify(snsService, times(1)).notifyUserOnUpdateProduct(List.of("user1@example.com"));
//    }
//    
//
//    @Test
//    void updateProduct_shouldNotUpdateImageIfNoImageFileProvided() throws IOException {
//        // Arrange
//        String existingName = "Old Product";
//        String newDescription = "Updated Description";
//        Products existingProduct = createProduct(1, existingName, "Old Description", "old-url", true, Collections.emptyList());
//
//        when(productRepository.findByName(existingName)).thenReturn(Optional.of(existingProduct));
//        when(productRepository.save(any(Products.class))).thenReturn(existingProduct);
//
//        // Act
//        Products result = productService.updateProduct(existingName, null, newDescription, null, null);
//
//        // Assert
//        assertEquals(newDescription, result.getDescription());
//        assertEquals("old-url", result.getImageUrl());
//        verify(s3Client, never()).putObject(any(PutObjectRequest.class), any());
//        verify(snsService, never()).notifyAdminOnUpdateProduct();
//        verify(snsService, never()).notifyUserOnUpdateProduct(anyList());
//    }
//
//    @Test
//    void updateProduct_shouldThrowInvalidInputExceptionIfProductNotFound() {
//        // Arrange
//        when(productRepository.findByName("NonExistentProduct")).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(InvalidInputException.class, () -> productService.updateProduct("NonExistentProduct", "New Name", "New Desc", null, true));
//        verify(productRepository, times(1)).findByName("NonExistentProduct");
//        verify(productRepository, never()).save(any(Products.class));
//        verify(s3Client, never()).putObject(any(PutObjectRequest.class), any());
//        verify(snsService, never()).notifyAdminOnUpdateProduct();
//        verify(snsService, never()).notifyUserOnUpdateProduct(anyList());
//    }
//
//    @Test
//    void getProductImage_shouldReturnImageBytesFromS3ById() {
//        // Arrange
//        Products product = createProduct(1, "Test", "Desc", "https://test-bucket.s3.us-east-1.amazonaws.com/product/test_image.jpg", true, Collections.emptyList());
//        byte[] expectedBytes = "test image data".getBytes();
//        ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes.fromByteArray(GetObjectResponse.builder().build(), expectedBytes);
//
//        when(productRepository.findById(1)).thenReturn(Optional.of(product));
//        when(s3Client.getObjectAsBytes(any())).thenReturn(responseBytes);
//
//        // Act
//        byte[] result = productService.getProductImage(1);
//
//        // Assert
//        assertArrayEquals(expectedBytes, result);
//        verify(productRepository, times(1)).findById(1);
//        verify(s3Client, times(1)).getObjectAsBytes(any());
//    }
//
//    @Test
//    void getProductImage_shouldThrowRuntimeExceptionIfProductNotFoundById() {
//        // Arrange
//        when(productRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(RuntimeException.class, () -> productService.getProductImage(1));
//        verify(productRepository, times(1)).findById(1);
//        verify(s3Client, never()).getObjectAsBytes(any());
//    }
//
//    @Test
//    void getProductImageByName_shouldReturnImageBytesFromS3ByName() {
//        // Arrange
//        Products product = createProduct(1, "Test Product", "Desc", "https://test-bucket.s3.us-east-amazonaws.com/product/test_product.png", true, Collections.emptyList());
//        byte[] expectedBytes = "another image data".getBytes();
//        ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes.fromByteArray(GetObjectResponse.builder().build(), expectedBytes);
//
//        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(product));
//        when(s3Client.getObjectAsBytes(any())).thenReturn(responseBytes);
//
//        // Act
//        byte[] result = productService.getProductImageByName("Test Product");
//
//        // Assert
//        assertArrayEquals(expectedBytes, result);
//        verify(productRepository, times(1)).findByName("Test Product");
//        verify(s3Client, times(1)).getObjectAsBytes(any());
//    }
//
//    @Test
//    void getProductImageByName_shouldThrowRuntimeExceptionIfProductNotFoundByName() {
//        // Arrange
//        when(productRepository.findByName("NonExistentProduct")).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(RuntimeException.class, () -> productService.getProductImageByName("NonExistentProduct"));
//        verify(productRepository, times(1)).findByName("NonExistentProduct");
//        verify(s3Client, never()).getObjectAsBytes(any());
//    }
//
//    @Test
//    void addSubscription_shouldAddSubscriptionForActiveProductAndNotifyUser() {
//        // Arrange
//        User user = createUser(1, "user@example.com", "testuser", UserRole.USER);
//        Products product = createProduct(10, "Subscribed Product", "Desc", "url", true, Collections.emptyList());
//
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(productRepository.save(any(Products.class))).thenReturn(product);
//        doNothing().when(snsService).notifyOnSubscribing("user@example.com", "testuser", "Subscribed Product");
//
//        // Act
//        Products result = productService.addSubscription(1, 10);
//
//        // Assert
//        assertEquals(1, result.getProductSubscriptionList().size());
//        assertTrue(result.getProductSubscriptionList().get(0).isOptIn());
//        assertEquals(user, result.getProductSubscriptionList().get(0).getUser());
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, times(1)).save(product);
//        verify(snsService, times(1)).notifyOnSubscribing("user@example.com", "testuser", "Subscribed Product");
//    }
//
//    @Test
//    void addSubscription_shouldReOptInIfAlreadySubscribedAndOptedOut() {
//        // Arrange
//        User user = createUser(1, "user@example.com", "testuser", UserRole.USER);
//        Products product = createProduct(10, "Subscribed Product", "Desc", "url", true, Collections.singletonList(
//                createSubscription(1, user, false)
//        ));
//
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(productRepository.save(any(Products.class))).thenReturn(product);
//
//        // Act
//        Products result = productService.addSubscription(1, 10);
//
//        // Assert
//        assertEquals(1, result.getProductSubscriptionList().size());
//        assertTrue(result.getProductSubscriptionList().get(0).isOptIn());
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, times(1)).save(product);
//        verify(snsService, times(1)).notifyOnSubscribing("user@example.com", "testuser", "Subscribed Product");
//    }
//
//    @Test
//    void addSubscription_shouldThrowInvalidProductExceptionIfProductNotFoundForSubscription() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.empty());
//        when(userRepository.findById(1)).thenReturn(Optional.of(createUser(1, "user@example.com", "testuser", UserRole.USER)));
//
//        // Act and Assert
//        assertThrows(InvalidProductException.class, () -> productService.addSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void addSubscription_shouldThrowUserNotFoundExceptionIfUserNotFoundForSubscription() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.of(createProduct(10, "Product", "Desc", "url", true, Collections.emptyList())));
//        when(userRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(UserNotFoundException.class, () -> productService.addSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void addSubscription_shouldThrowInvalidProductExceptionIfProductIsNotActiveForSubscription() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.of(createProduct(10, "Inactive Product", "Desc", "url", false, Collections.emptyList())));
//        when(userRepository.findById(1)).thenReturn(Optional.of(createUser(1, "user@example.com", "testuser", UserRole.USER)));
//
//        // Act and Assert
//        assertThrows(InvalidProductException.class, () -> productService.addSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void addSubscription_shouldThrowInvalidSubscriptionExceptionIfAlreadySubscribedAndActive() {
//        // Arrange
//        User user = createUser(1, "user@example.com", "testuser", UserRole.USER);
//        Products product = createProduct(10, "Subscribed Product", "Desc", "url", true, Collections.singletonList(
//                createSubscription(1, user, true)
//        ));
//
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//
//        // Act and Assert
//        assertThrows(InvalidSubscriptionException.class, () -> productService.addSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void removeSubscription_shouldRemoveSubscriptionForActiveProductAndNotifyUser() {
//        // Arrange
//        User user = createUser(1, "user@example.com", "testuser", UserRole.USER);
//        Products product = createProduct(10, "Subscribed Product", "Desc", "url", true, Collections.singletonList(
//                createSubscription(1, user, true)
//        ));
//
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(productRepository.save(any(Products.class))).thenReturn(product);
//        doNothing().when(snsService).notifyOnUnSubscribing("user@example.com", "testuser", "Subscribed Product");
//
//        // Act
//        Products result = productService.removeSubscription(1, 10);
//
//        // Assert
//        assertEquals(1, result.getProductSubscriptionList().size());
//        assertFalse(result.getProductSubscriptionList().get(0).isOptIn());
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, times(1)).save(product);
//        verify(snsService, times(1)).notifyOnUnSubscribing("user@example.com", "testuser", "Subscribed Product");
//    }
//
//    @Test
//    void removeSubscription_shouldThrowInvalidProductExceptionIfProductNotFoundForUnsubscription() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.empty());
//        when(userRepository.findById(1)).thenReturn(Optional.of(createUser(1, "user@example.com", "testuser", UserRole.USER)));
//
//        // Act and Assert
//        assertThrows(InvalidProductException.class, () -> productService.removeSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnUnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void removeSubscription_shouldThrowUserNotFoundExceptionIfUserNotFoundForUnsubscription() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.of(createProduct(10, "Product", "Desc", "url", true, Collections.emptyList())));
//        when(userRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(UserNotFoundException.class, () -> productService.removeSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnUnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void removeSubscription_shouldThrowInvalidProductExceptionIfProductIsNotActiveForUnsubscription() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.of(createProduct(10, "Inactive Product", "Desc", "url", false, Collections.emptyList())));
//        when(userRepository.findById(1)).thenReturn(Optional.of(createUser(1, "user@example.com", "testuser", UserRole.USER)));
//
//        // Act and Assert
//        assertThrows(InvalidProductException.class, () -> productService.removeSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnUnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void removeSubscription_shouldThrowInvalidSubscriptionExceptionIfNotSubscribed() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.of(createProduct(10, "Product", "Desc", "url", true, Collections.emptyList())));
//        when(userRepository.findById(1)).thenReturn(Optional.of(createUser(1, "user@example.com", "testuser", UserRole.USER)));
//
//        // Act and Assert
//        assertThrows(InvalidSubscriptionException.class, () -> productService.removeSubscription(1, 10));
//        verify(productRepository, times(1)).findById(10);
//        verify(userRepository, times(1)).findById(1);
//        verify(productRepository, never()).save(any(Products.class));
//        verify(snsService, never()).notifyOnUnSubscribing(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void getSubscriptionList_shouldReturnListOfActiveSubscriptionsForProduct() {
//        // Arrange
//        User user1 = createUser(1, "user1@example.com", "user1", UserRole.USER);
//        User user2 = createUser(2, "user2@example.com", "user2", UserRole.USER);
//        Products product = createProduct(10, "Product", "Desc", "url", true, Arrays.asList(
//                createSubscription(1, user1, true),
//                createSubscription(2, user2, true),
//                createSubscription(3, createUser(3, "user3@example.com", "user3", UserRole.USER), false) // Opted out
//        ));
//
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//
//        // Act
//        List<ProductSubscription> result = productService.getSubscriptionList(10);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.stream().allMatch(ProductSubscription::isOptIn));
//        verify(productRepository, times(1)).findById(10);
//    }
//
//    @Test
//    void getSubscriptionList_shouldReturnEmptyListIfNoActiveSubscriptions() {
//        // Arrange
//        Products product = createProduct(10, "Product", "Desc", "url", true, Collections.singletonList(
//                createSubscription(1, createUser(1, "user@example.com", "testuser", UserRole.USER), false)
//        ));
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//
//        // Act
//        List<ProductSubscription> result = productService.getSubscriptionList(10);
//
//        // Assert
//        assertTrue(result.isEmpty());
//        verify(productRepository, times(1)).findById(10);
//    }
//
//    @Test
//    void getSubscriptionList_shouldThrowInvalidProductExceptionIfProductNotFoundForSubscriptionList() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(InvalidProductException.class, () -> productService.getSubscriptionList(10));
//        verify(productRepository, times(1)).findById(10);
//    }
//
//    @Test
//    void getProductSubscriptionList_shouldReturnListOfActiveSubscribedProductsForUser() {
//        // Arrange
//        ProductViewDTO product1 = createProductViewDTO(1, "Subscribed Product 1", "Desc", "url", 4.0);
//        product1.setIsactive(true);
//        ProductViewDTO product2 = createProductViewDTO(2, "Subscribed Product 2", "Desc", "url", 4.5);
//        product2.setIsactive(true);
//        ProductViewDTO inactiveProduct = createProductViewDTO(3, "Inactive Subscribed", "Desc", "url", 3.5);
//        inactiveProduct.setIsactive(false);
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(createUser(1, "user@example.com", "testuser", UserRole.USER)));
//        when(productViewRepo.getSubscribedListByUser(1)).thenReturn(Arrays.asList(product1, product2, inactiveProduct));
//
//        // Act
//        List<ProductViewDTO> result = productService.getProductSubscriptionList(1);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.stream().allMatch(ProductViewDTO::isIsactive));
//        assertEquals("Subscribed Product 1", result.get(0).getName());
//        assertEquals("Subscribed Product 2", result.get(1).getName());
//        verify(userRepository, times(1)).findById(1);
//        verify(productViewRepo, times(1)).getSubscribedListByUser(1);
//    }
//
//    @Test
//    void getProductSubscriptionList_shouldReturnEmptyListIfNoSubscribedProductsForUser() {
//        // Arrange
//        when(userRepository.findById(1)).thenReturn(Optional.of(createUser(1, "user@example.com", "testuser", UserRole.USER)));
//        when(productViewRepo.getSubscribedListByUser(1)).thenReturn(Collections.emptyList());
//
//        // Act
//        List<ProductViewDTO> result = productService.getProductSubscriptionList(1);
//
//        // Assert
//        assertTrue(result.isEmpty());
//        verify(userRepository, times(1)).findById(1);
//        verify(productViewRepo, times(1)).getSubscribedListByUser(1);
//    }
//
//    @Test
//    void getProductSubscriptionList_shouldThrowUserNotFoundExceptionIfUserNotFoundForSubscribedList() {
//        // Arrange
//        when(userRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(UserNotFoundException.class, () -> productService.getProductSubscriptionList(1));
//        verify(userRepository, times(1)).findById(1);
//        verify(productViewRepo, never()).getSubscribedListByUser(anyInt());
//    }
//
//    @Test
//    void findTopSubscribedProduct_shouldReturnListOfTopSubscribedProducts() {
//        // Arrange
//        ProductViewDTO top1 = createProductViewDTO(1, "Top Product 1", "Desc", "url", 4.5);
//        top1.setSubscription_count(10);
//        ProductViewDTO top2 = createProductViewDTO(2, "Top Product 2", "Desc", "url", 4.2);
//        top2.setSubscription_count(8);
//
//        when(productViewRepo.findTopSubscribedProduct()).thenReturn(Arrays.asList(top1, top2));
//
//        // Act
//        List<ProductViewDTO> result = productService.findTopSubscribedProduct();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("Top Product 1", result.get(0).getName());
//        assertEquals(10, result.get(0).getSubscription_count());
//        assertEquals("Top Product 2", result.get(1).getName());
//        assertEquals(8, result.get(1).getSubscription_count());
//        verify(productViewRepo, times(1)).findTopSubscribedProduct();
//    }
//
//    @Test
//    void findTopSubscribedProduct_shouldReturnEmptyListIfNoSubscribedProducts() {
//        // Arrange
//        when(productViewRepo.findTopSubscribedProduct()).thenReturn(Collections.emptyList());
//
//        // Act
//        List<ProductViewDTO> result = productService.findTopSubscribedProduct();
//
//        // Assert
//        assertTrue(result.isEmpty());
//        verify(productViewRepo, times(1)).findTopSubscribedProduct();
//    }
//
//    @Test
//    void findTopRatedProducts_shouldReturnListOfTopRatedProducts() {
//        // Arrange
//        ProductViewDTO rated1 = createProductViewDTO(1, "Rated Product 1", "Desc", "url", 4.8);
//        ProductViewDTO rated2 = createProductViewDTO(2, "Rated Product 2", "Desc", "url", 4.6);
//
//        when(productViewRepo.findTopRatedProducts()).thenReturn(Arrays.asList(rated1, rated2));
//
//        // Act
//        List<ProductViewDTO> result = productService.findTopRatedProducts();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("Rated Product 1", result.get(0).getName());
//        assertEquals(4.8, result.get(0).getAvg_rating());
//        assertEquals("Rated Product 2", result.get(1).getName());
//        assertEquals(4.6, result.get(1).getAvg_rating());
//        verify(productViewRepo, times(1)).findTopRatedProducts();
//    }
//
//    @Test
//    void findTopRatedProducts_shouldReturnEmptyListIfNoRatedProducts() {
//        // Arrange
//        when(productViewRepo.findTopRatedProducts()).thenReturn(Collections.emptyList());
//
//        // Act
//        List<ProductViewDTO> result = productService.findTopRatedProducts();
//
//        // Assert
//        assertTrue(result.isEmpty());
//        verify(productViewRepo, times(1)).findTopRatedProducts();
//    }
//
//    @Test
//    void getUsersSubscribedToProduct_shouldReturnListOfUsersSubscribedToProduct() {
//        // Arrange
//        User user1 = createUser(1, "user1@example.com", "user1", UserRole.USER);
//        User user2 = createUser(2, "user2@example.com", "user2", UserRole.USER);
//        Products product = createProduct(10, "Product", "Desc", "url", true, Arrays.asList(
//                createSubscription(1, user1, true),
//                createSubscription(2, user2, true),
//                createSubscription(3, createUser(3, "user3@example.com", "user3", UserRole.USER), false)
//        ));
//
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//
//        // Act
//        List<User> result = productService.getUsersSubscribedToProduct(10);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(user1));
//        assertTrue(result.contains(user2));
//        verify(productRepository, times(1)).findById(10);
//    }
//
//    @Test
//    void getUsersSubscribedToProduct_shouldReturnEmptyListIfNoSubscribers() {
//        // Arrange
//        Products product = createProduct(10, "Product", "Desc", "url", true, Collections.singletonList(
//                createSubscription(1, createUser(1, "user@example.com", "testuser", UserRole.USER), false)
//        ));
//        when(productRepository.findById(10)).thenReturn(Optional.of(product));
//
//        // Act
//        List<User> result = productService.getUsersSubscribedToProduct(10);
//
//        // Assert
//        assertTrue(result.isEmpty());
//        verify(productRepository, times(1)).findById(10);
//    }
//
//    @Test
//    void getUsersSubscribedToProduct_shouldThrowInvalidProductExceptionIfProductNotFoundForSubscribers() {
//        // Arrange
//        when(productRepository.findById(10)).thenReturn(Optional.empty());
//
//        // Act and Assert
//        assertThrows(InvalidProductException.class, () -> productService.getUsersSubscribedToProduct(10));
//        verify(productRepository, times(1)).findById(10);
//    }
//
//    // Helper methods to create entities and DTOs for cleaner tests
//    private Products createProduct(int id, String name, String description, String imageUrl, boolean isActive, List<ProductSubscription> subscriptions) {
//        Products product = new Products();
//        product.setProductid(id);
//        product.setName(name);
//        product.setDescription(description);
//        product.setImageUrl(imageUrl);
//        product.setIsActive(isActive);
//        product.setProductSubscriptionList(subscriptions);
//        return product;
//    }
//
//    private ProductViewDTO createProductViewDTO(int id, String name, String description, String imageUrl, double avgRating) {
//        ProductViewDTO dto = new ProductViewDTO();
//        dto.setProductid(id);
//        dto.setName(name);
//        dto.setDescription(description);
//        dto.setImageUrl(imageUrl);
//        dto.setAvg_rating(avgRating);
//        return dto;
//    }
//
//    private User createUser(int id, String email, String nickName, UserRole userRole) {
//        User user = new User();
//        user.setUserID(id);
//        user.setEmail(email);
//        user.setNickName(nickName);
//        user.setUserRole(userRole);
//        return user;
//    }
//
//    private ProductSubscription createSubscription(int id, User user, boolean optIn) {
//        ProductSubscription subscription = new ProductSubscription();
//        subscription.setSubscriptionId(id);
//        subscription.setUser(user);
//        subscription.setOptIn(optIn);
//        subscription.setUpdatedOn(LocalDateTime.now());
//        return subscription;
//    }
//}