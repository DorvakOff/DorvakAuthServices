import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Error404Component} from "./pages/error404/error404.component";

export let routes: Routes = [
  {
    path: '**',
    component: Error404Component,
    data: {
      meta: {
        title: '404 - Page not found',
        component: 'Error404Component'
      }
    }
  }];

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
