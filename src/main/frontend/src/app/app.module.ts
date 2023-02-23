import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgcCookieConsentConfig, NgcCookieConsentModule} from "ngx-cookieconsent";
import {AppRoutingModule} from "./app-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatButtonModule} from "@angular/material/button";
import {MatMenuModule} from "@angular/material/menu";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {AppComponent} from "./app.component";
import {Error404Component} from "./pages/error404/error404.component";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {BusyIndicatorComponent} from "./components/shared/busy-indicator/busy-indicator.component";
import {ButtonComponent} from "./components/shared/button/button.component";
import {SwitchButtonComponent} from "./components/shared/switch-button/switch-button.component";
import {AlertComponent} from "./components/alerts/alert/alert.component";
import {AlertBoxComponent} from "./components/alerts/alert-box/alert-box.component";
import {MenuComponent} from "./components/menu/menu.component";
import { HomeComponent } from './pages/home/home.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { LoginComponent } from './pages/login/login.component';


const cookieConfig: NgcCookieConsentConfig = {
    cookie: {
        domain: document.baseURI.includes("localhost") ? 'localhost' : document.domain,
    },
    position: 'bottom-right',
    theme: 'block',
    palette: {
        popup: {
            background: '#242830',
            text: '#ffffff',
            link: '#ffffff'
        },
        button: {
            background: '#0270d7',
            text: '#ffffff',
            border: 'transparent'
        }
    },
    type: 'info',
    content: {
        message: 'This website uses cookies to ensure you get the best experience on our website.',
        dismiss: 'Got it!',
        link: 'Learn more',
        href: '/privacy-policy',
        policy: 'Cookie Policy'
    }
}

export function HttpLoaderFactory(httpClient: HttpClient) {
    return new TranslateHttpLoader(httpClient)
}

@NgModule({
    declarations: [
      AppComponent,
      Error404Component,
      BusyIndicatorComponent,
      ButtonComponent,
      SwitchButtonComponent,
      AlertComponent,
      AlertBoxComponent,
      MenuComponent,
      HomeComponent,
      SettingsComponent,
      LoginComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        NgcCookieConsentModule.forRoot(cookieConfig),
        ReactiveFormsModule,
        DragDropModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
        BrowserAnimationsModule,
        MatMenuModule,
        MatButtonModule,
        MatSlideToggleModule,
        MatProgressSpinnerModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {

}

