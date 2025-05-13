
// import { Component, OnInit } from '@angular/core';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { AuthService } from '../../services/auth.service';
// import { ActivatedRoute, Router, RouterModule } from '@angular/router';
// import { CommonModule } from '@angular/common';
// import { ReactiveFormsModule } from '@angular/forms';
// import { RecaptchaModule } from 'ng-recaptcha-2';

// @Component({
//     selector: 'app-reset',
//     standalone: true,
//     imports: [CommonModule, ReactiveFormsModule, RecaptchaModule, RouterModule],
//     templateUrl: './reset.component.html',
//     styleUrls: ['./reset.component.css'],
//     providers : [AuthService]
// })
// export class ResetComponent implements OnInit {
//     resetPasswordForm!: FormGroup;
//     emailFromQuery: string | null = null;

//     constructor(private fb: FormBuilder, private authService: AuthService, private route: ActivatedRoute, private router: Router) {}

//     ngOnInit() {
//         this.resetPasswordForm = this.fb.group({
//             password: [
//                 '',
//                 [
//                     Validators.required,
//                     Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$/)
//                 ]
//             ],
//             confirmPassword: ['', [Validators.required]],
//             captchaResponse: ['']
//         }, { validator: this.passwordMatchValidator }); 

//         this.route.queryParams.subscribe(params => {
//             if (params['email']) {
//                 this.emailFromQuery = params['email'];

//             } else {
//                 alert('Invalid reset link.');
//                 this.resetPasswordForm.disable();
//             }
//         });
//     }

//     passwordMatchValidator(formGroup: FormGroup): void {
//         const password = formGroup.get('password')?.value;
//         const confirmPassword = formGroup.get('confirmPassword')?.value;

//         if (password && confirmPassword && password !== confirmPassword) {
//             formGroup.get('confirmPassword')?.setErrors({ mismatch: true });
//         } else {
//             formGroup.get('confirmPassword')?.setErrors(null);
//         }
//     }

//     onCaptchaResolved(captchaResponse: string | null) {
//         this.resetPasswordForm.patchValue({ captchaResponse });
//         console.log('Captcha Response:', captchaResponse);
//     }

//     resetPassword() {
//         if (!this.resetPasswordForm.value.captchaResponse) {
//             alert("Please verify that you are not a robot.");
//             return;
//         }

//         if (this.resetPasswordForm.invalid) {
//             alert("Please fill in all required fields and ensure the password meets the criteria.");
//             return;
//         }

//         if (!this.emailFromQuery) {
//             alert('No email provided for password reset.');
//             return;
//         }

//         const payload = {
//             email: this.emailFromQuery,
//             newPassword: this.resetPasswordForm.value.password,
//             confirmPassword: this.resetPasswordForm.value.confirmPassword,
//             captchaResponse: this.resetPasswordForm.value.captchaResponse
//         };

//         this.authService.resetPassword(payload).subscribe({
//             next: (response: string) => {
//                 alert(response);
//                 setTimeout(() => {
//                     this.router.navigate(['/signin']);
//                 }, 1000);
//             },
//             error: (error) => {
//                 alert('Error resetting password: ' + error.error.text);
//                 console.error('Error:', error);
//             }
//         });
//     }
// }


import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RecaptchaModule } from 'ng-recaptcha-2';
import { CognitoUser, CognitoUserPool } from 'amazon-cognito-identity-js';

@Component({
    selector: 'app-reset',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RecaptchaModule, RouterModule, FormsModule],
    templateUrl: './reset.component.html',
    styleUrls: ['./reset.component.css'],
    providers : [AuthService]
})
export class ResetComponent implements OnInit {
    email: string | null = localStorage.getItem('forgotEmail');;
    code: string = '';


    onCaptchaResolved($event: string|null) {
        throw new Error('Method not implemented.');
    }
    //resetPasswordForm!: FormGroup;
    //emailFromQuery = localStorage.getItem('forgotEmail');
    //email = localStorage.getItem('forgotEmail');
    newPassword = '';
    confirmPassword = '';
    //verificationCode = '';


    poolData = {
        UserPoolId: 'us-east-1_xfDGaQqxz',
      ClientId: '3htlbjbquppu1gj0j1clc39e54'
      };
      
    constructor(private route: ActivatedRoute, private router : Router) {}

    ngOnInit(): void {
        this.route.queryParams.subscribe((params) => {
            this.code = params['code'];
        })
    }
      

codeTouched: any;

    //constructor(private fb: FormBuilder, private authService: AuthService, private route: ActivatedRoute, private router: Router) {}

    // ngOnInit() {
    //     this.resetPasswordForm = this.fb.group({
    //         password: [
    //             '',
    //             [
    //                 Validators.required,
    //                 Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$/)
    //             ]
    //         ],
    //         confirmPassword: ['', [Validators.required]],
    //         captchaResponse: ['']
    //     }, { validator: this.passwordMatchValidator }); 

        // this.route.queryParams.subscribe(params => {
        //     if (params['email']) {
        //         this.emailFromQuery = params['email'];
                
        //     } else {
        //         alert('Invalid reset link.');
        //         this.resetPasswordForm.disable();
        //     }
        // });
    // }

    // passwordMatchValidator(formGroup: FormGroup): void {
    //     const password = formGroup.get('password')?.value;
    //     const confirmPassword = formGroup.get('confirmPassword')?.value;

    //     if (password && confirmPassword && password !== confirmPassword) {
    //         formGroup.get('confirmPassword')?.setErrors({ mismatch: true });
    //     } else {
    //         formGroup.get('confirmPassword')?.setErrors(null);
    //     }
    // }

    // onCaptchaResolved(captchaResponse: string | null) {
    //     this.resetPasswordForm.patchValue({ captchaResponse });
    //     console.log('Captcha Response:', captchaResponse);
    // }

    resetPassword(): void {
        // if (!this.resetPasswordForm.value.captchaResponse) {
        //     alert("Please verify that you are not a robot.");
        //     return;
        // }
        //const email = localStorage.getItem('forgotEmail');
        if(!this.email){
            alert("Email not found");
            return;
        }

        if(this.newPassword !== this.confirmPassword){
            alert('Passwords do not match');
            return;
        }


        const userPool = new CognitoUserPool(this.poolData);

        const userData = {
            Username: this.email,
            Pool: userPool
        };

        const cognitoUser = new CognitoUser(userData);

        cognitoUser.confirmPassword(this.code, this.newPassword, {
            onSuccess: () => {alert('Password reset successful');
                localStorage.removeItem('forgotEmail');
            },
            onFailure: (err) => {
                console.error('Reset failed', err);
            }
        })
        // if (this.resetPasswordForm.invalid) {
        //     alert("Please fill in all required fields and ensure the password meets the criteria.");
        //     return;
        // }

        // if (!this.emailFromQuery) {
        //     alert('No email provided for password reset.');
        //     return;
        // }

        // const payload = {
        //     email: localStorage.getItem('forgotEmail');
        //     // newPassword: this.resetPasswordForm.value.password,
        //     // confirmPassword: this.resetPasswordForm.value.confirmPassword,
        //     captchaResponse: this.resetPasswordForm.value.captchaResponse
        // };

        // this.authService.resetPassword(payload).subscribe({
        //     next: (response: string) => {
        //         alert(response);
        //         setTimeout(() => {
        //             this.router.navigate(['/signin']);
        //         }, 1000);
        //     },
        //     error: (error) => {
        //         alert('Error resetting password: ' + error.error.text);
        //         console.error('Error:', error);
        //     }
        // });
        this.router.navigate(['/signin']);
    }
    


// function resetPassword() {
//     throw new Error('Function not implemented.');
// }
}
