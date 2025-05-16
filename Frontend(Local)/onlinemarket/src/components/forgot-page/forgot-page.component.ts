// import { Component, OnInit } from '@angular/core';
// import { AuthService } from '../../services/auth.service';
// import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
// import { RecaptchaModule } from 'ng-recaptcha-2';
// import { RouterModule } from '@angular/router';
// import { CommonModule } from '@angular/common';
 
// @Component({
//   selector: 'app-forgot-page',
//   imports: [ReactiveFormsModule, RecaptchaModule, RouterModule,CommonModule],
//   standalone: true,
//   templateUrl: './forgot-page.component.html',
//   styleUrls: ['./forgot-page.component.css'],
//   providers: [AuthService]
// })
// export class ForgotPageComponent implements OnInit {
//   forgotForm!: FormGroup;
//   captchaResponse: string | null = null;
//   message: string = '';
 
//   constructor(private fb: FormBuilder, private authService: AuthService) { }
 
//   ngOnInit(): void {
//     this.forgotForm = this.fb.group({
//       email: ['', [Validators.required, Validators.email]],
//       captchaResponse: ['', Validators.required]
//     });
//   }
 
//   onCaptchaResolved(captchaResponse: string | null) {
//     this.forgotForm.patchValue({ captchaResponse });
//     console.log('Captcha Response:', captchaResponse);
//   }
 
//   onSubmit() {
//     if (this.forgotForm.invalid) {
//       alert("Please fill in all required fields and verify the captcha.");
//       return;
//     }
 
//     const email = this.forgotForm.value.email;
 
//     console.log("Requesting reset link for:", email);
 
//     this.authService.forgotPassword(email).subscribe({
//       next: (resetLink: string) => {
//         console.log("Reset Link Generated:", resetLink);
//         alert("Reset link has been generated in the console.");
//         this.message = "";
//         this.forgotForm.reset();
//       },
//       error: (error) => {
//         console.error("Error:", error);
//         if (error.status === 404) {
//           alert("User not found");
//         } else {
//           alert("Something went wrong");
//         }
//         this.message = "";
//         this.forgotForm.get('captchaResponse')?.setValue(null);
//       }
//     });
//   }
// }
 


// import { Component, OnInit } from '@angular/core';
// import { AuthService } from '../../services/auth.service';
// import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
// import { RecaptchaModule } from 'ng-recaptcha-2';
// import { Router, RouterModule } from '@angular/router';
// import { CommonModule } from '@angular/common';
//  import { CognitoUser, CognitoUserPool } from 'amazon-cognito-identity-js';

// @Component({
//   selector: 'app-forgot-page',
//   imports: [ReactiveFormsModule, RecaptchaModule, RouterModule,CommonModule],
//   standalone: true,
//   templateUrl: './forgot-page.component.html',
//   styleUrls: ['./forgot-page.component.css'],
//   providers: [AuthService]
// })
// export class ForgotPageComponent implements OnInit {
//   forgotForm!: FormGroup;
//   captchaResponse: string | null = null;
//   message: string = '';


//     email: string = '';
  
//     userPool = new CognitoUserPool({
    
//     //   UserPoolId: 'us-east-1_xfDGaQqxz',
//     // ClientId: '3htlbjbquppu1gj0j1clc39e54'

//     UserPoolId: 'us-east-1_JtK3G3BUj',
//     ClientId: '67tgtkhhr55r4mpbb3vr3llg9e'

//     });

//   constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

//   ngOnInit(): void {
//     this.forgotForm = this.fb.group({
//       email: ['', [Validators.required, Validators.email]],
//       captchaResponse: ['', Validators.required] 
//     });
//   }

//   onCaptchaResolved(captchaResponse: string | null) {
//     this.forgotForm.patchValue({ captchaResponse });
//     console.log('Captcha Response:', captchaResponse);
//   }

//   onSubmit() {
//     if (this.forgotForm.invalid) {
//       alert("Please fill in all required fields and verify the captcha.");
//       return;
//     }


//     const userData = {
//           Username: this.email,
//           Pool: this.userPool
//         };
    
//     const cognitoUser = new CognitoUser(userData);
//     console.log(this.email);

//         this.authService.forgotPassword(this.email).subscribe({
//        next: (response) => {
//         console.log("successful");
            

         
//          localStorage.setItem('forgotEmail', this.email);

//       },
//       error: (error) => {
//         console.error("Error initiating password reset:", error);
//         if (error.status === 404) {
//           alert("User not found. Please check your email address.");
//         } else {
//           alert("Something went wrong. Please try again later.");
//         }
//         this.forgotForm.get('captchaResponse')?.setValue(null); 
//       }})

//     cognitoUser.forgotPassword({
//       onSuccess: function(data: any) {
//         console.log("Code sent successfully", data);
//         alert("Code has been sent.");

//       },
//       onFailure: function(err: any) {
//         console.error("Error sending code", err);
//       }
//     })
//     localStorage.setItem('forgotEmail',this.email);
//     this.router.navigate(['/verify-email']);

    
//        (error: { status: any; }) => {
//         console.error("Error:", error);
//         if (error.status === 404) {
//           alert("User not found");
//         } else {
//           alert("Something went wrong");
//         }
//         this.message = "";
//         this.forgotForm.get('captchaResponse')?.setValue(null); 
//       }
    
//   }
// }






import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RecaptchaModule, RecaptchaComponent } from 'ng-recaptcha-2';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CognitoUser, CognitoUserPool } from 'amazon-cognito-identity-js';
import { environment } from '../../environment/environment';

@Component({
  selector: 'app-forgot-page',
  imports: [ReactiveFormsModule, RecaptchaModule, RouterModule,CommonModule],
  standalone: true,
  templateUrl: './forgot-page.component.html',
  styleUrls: ['./forgot-page.component.css'],
  providers: [AuthService]
})
export class ForgotPageComponent implements OnInit {
  forgotForm!: FormGroup;
  captchaResponse: string | null = null;
  message: string = '';
  emailError: string = '';
  recaptchaError: string = ''; 


  email: string = '';
  
  userPool = new CognitoUserPool({
    UserPoolId: environment.UserPoolId,
    ClientId: environment.ClientId
  });

  @ViewChild(RecaptchaComponent) recaptchaComponent!: RecaptchaComponent; 

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      captchaResponse: ['', Validators.required] 
    });

    // Clear email error when email input changes or form becomes invalid
    this.forgotForm.get('email')?.valueChanges.subscribe(() => {
      this.emailError = '';
      if (this.forgotForm.invalid) {
        this.message = ''; // Clear general message
      }
    });
  }

  onCaptchaResolved(captchaResponse: string | null) {
    this.forgotForm.patchValue({ captchaResponse });
    console.log('Captcha Response:', captchaResponse);
    this.recaptchaError = '';
  }

  onSubmit() {
    this.recaptchaError = '';
    this.emailError = '';

    if (!this.forgotForm.get('captchaResponse')?.value) {
      this.recaptchaError = 'Please verify that you are not a robot.';
      console.log('Recaptcha is not clicked');
    } else {
      this.recaptchaError = ''; // Clear Recaptcha error
    }

    if (this.forgotForm.invalid) {
      //alert("Please fill in all required fields and verify the captcha.");
      return;
    }

    // const email = this.forgotForm.value.email;
    // console.log("Requesting reset link for:", email);

    const userData = {
          Username: this.email,
          Pool: this.userPool
        };
    
    const cognitoUser = new CognitoUser(userData);
    console.log(this.email);

        this.authService.forgotPassword(this.email).subscribe({
       next: (response) => {
        console.log("successful");
        this.message = "Reset link has been generated in the console.";
        this.emailError = '';
        this.recaptchaError = '';
        this.forgotForm.reset(); 
        this.resetRecaptcha(); 
        
        cognitoUser.forgotPassword({
          onSuccess: function(data: any) {
            console.log("Code sent successfully", data);
            alert("Code has been sent.");
    
          },
          onFailure: function(err: any) {
            console.error("Error sending code", err);
          }
        })

        this.router.navigate(['/verify-email']);
         localStorage.setItem('forgotEmail', this.email);
         this.forgotForm.get('captchaResponse')?.setValue(null); 

      },
      error: (error) => {
        console.error("Error initiating password reset:", error);

        if (error.status === 404) {
          // alert("User not found");
          this.emailError = "User not found";
          this.message = '';
        } else {
          // alert("Something went wrong");
          this.message = "Something went wrong"; // A general error message.
          this.emailError = '';
        }
        this.recaptchaError = '';
        this.message = "";
        this.forgotForm.get('captchaResponse')?.setValue(null); 
        this.resetRecaptcha();
        //this.forgotForm.get('captchaResponse')?.setValue(null); 
        
      }});

    // cognitoUser.forgotPassword({
    //   onSuccess: function(data: any) {
    //     console.log("Code sent successfully", data);
    //     alert("Code has been sent.");

    //   },
    //   onFailure: function(err: any) {
    //     console.error("Error sending code", err);
    //   }
    // })
    // localStorage.setItem('forgotEmail',this.email);
    // this.router.navigate(['/verify-email']);

    
      //  (error: { status: any; }) => {
      //   console.error("Error:", error);
      //   if (error.status === 404) {
      //     alert("User not found");
      //   } else {
      //     alert("Something went wrong");
      //   }
      //   this.message = "";
      //   this.forgotForm.get('captchaResponse')?.setValue(null); 
      // }
    
  }
  private resetRecaptcha() {
    if (this.recaptchaComponent) {
      this.recaptchaComponent.reset(); // Use the reset() method
    }
  }
}
