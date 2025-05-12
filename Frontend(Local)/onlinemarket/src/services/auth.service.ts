
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { CognitoUser, CognitoUserPool, AuthenticationDetails, CognitoUserAttribute, CognitoUserSession } from 'amazon-cognito-identity-js';

const poolData = {
    UserPoolId: 'us-east-1_v6dUUkwbr',
    ClientId: '5b5k6sbfttg4koe5skac2hipbo'
};

const userPool = new CognitoUserPool(poolData);

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient) {}

    signUp(email: string, password: string): Promise<any> {
        
        const attributeList: CognitoUserAttribute[] = [];

        const dataEmail = {
            Name: 'email',
            Value: email
        };
        
        const attributeEmail = new CognitoUserAttribute(dataEmail);
        attributeList.push(attributeEmail);

        return new Promise((resolve,reject) => {
            userPool.signUp(email, password, [], null!, (err, result) => {
                if (err) reject(err);
                else resolve(result);
            });
        });
    }


    signIn(email: string, password: string): Promise<CognitoUserSession> {
        const authenticationDetails = new AuthenticationDetails({Username: email, Password: password});

        const userData = {
            Username: email,
            Pool: userPool
        };

        const cognitoUser = new CognitoUser(userData);

        return new Promise((resolve, reject) => {
            cognitoUser.authenticateUser(authenticationDetails, {onSuccess: session => resolve(session),
                onFailure: err => reject(err)
            });
        });
    }


    signOut(): void {
        const user = userPool.getCurrentUser();
        if(user) user.signOut();
    }

    private generateResetLinkUrl = 'http://127.0.0.1:3000/OMP/generate-reset-link';
    private apiUrl = 'http://127.0.0.1:3000/OMP/reset-password';

    private loggedInSource = new BehaviorSubject<boolean>(false);
    loggedIn$ = this.loggedInSource.asObservable();

    forgotPassword(email: string): Observable<string> {
        const params = new HttpParams().set('email', email);
        return this.http.post(this.generateResetLinkUrl, {}, { params, responseType: 'text' });
    }

    resetPassword(payload: any): Observable<string> { 
        return this.http.post(this.apiUrl, payload, { responseType: 'text' }); 
    }
}