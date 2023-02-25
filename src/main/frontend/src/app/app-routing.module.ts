import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Error404Component} from "./pages/error404/error404.component";
import {HomeComponent} from "./pages/home/home.component";
import {SettingsComponent} from "./pages/settings/settings.component";
import {LoginComponent} from "./pages/login/login.component";
import {VerificationComponent} from "./pages/verification/verification.component";

export let routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        data: {
            meta: {
                title: 'Home',
            }
        }
    },
    {
        path: 'settings',
        component: SettingsComponent,
        data: {
            meta: {
                title: 'Settings',
            }
        }
    },
    {
        path: 'login',
        component: LoginComponent,
        data: {
            meta: {
                title: 'Login'
            }
        }
    },
    {
        path: 'verify/:token',
        component: VerificationComponent,
        data: {
            meta: {
                title: 'Verify'
            }
        }
    },
    {
        path: '**',
        component: Error404Component,
        data: {
            meta: {
                title: '404 - Page not found',
            }
        }
    }
]

@NgModule({
    imports: [RouterModule.forRoot(routes, {
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled',
        initialNavigation: 'enabledBlocking',
        paramsInheritanceStrategy: 'always',
    })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
