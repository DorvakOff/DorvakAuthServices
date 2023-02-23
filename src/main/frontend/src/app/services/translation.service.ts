import {Injectable} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class TranslationService {

  defaultLang: Language = {
    code: 'en',
    name: 'English'
  }

  languagesConfig: Language[] = [
    {
      code: "fr",
      name: "Français"
    },
    {
      code: "en",
      name: "English"
    }
  ]

  languages: string[] = this.languagesConfig.map((lang: any) => lang.code)

  constructor(private translate: TranslateService) {
    translate.addLangs(this.languages)
    translate.setDefaultLang(this.defaultLang.code)

    translate.use(this.getLang())
    localStorage.setItem('lang', translate.currentLang)
  }

  changeLang(lang: string) {
    localStorage.setItem('lang', lang)
    this.translate.use(lang)
  }

  getLang(): string {
    let found = localStorage.getItem('lang') || this.translate.getBrowserLang() || this.defaultLang.code
    return this.languages.includes(found) ? found : this.defaultLang.code
  }

  getLanguageName(code: string): string {
    return this.getLanguage(code).name
  }

  getLanguage(code: string): any {
    code = code.toLowerCase()
    let found = this.languagesConfig.filter((lang: any) => lang.code == code)[0]
    if (found) {
      return found
    } else {
      return this.defaultLang.name
    }
  }

  getFlag(code: string): string {
    return `assets/images/flags/${code.toLowerCase()}_flag.png`
  }
}

export interface Language {
  code: string
  name: string
}
