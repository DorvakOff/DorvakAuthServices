let button = document.getElementById('login')

let css = `
#login {
    background-color: #4CAF50;
    border: none;
    color: white;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    border-radius: 8px;
} 

#login:hover { 
    cursor: pointer; 
    background-color: #3e8e41;
}
`

let style = document.createElement('style')

if (style.styleSheet) {
    style.styleSheet.cssText = css
} else {
    style.appendChild(document.createTextNode(css))
}

document.getElementsByTagName('head')[0].appendChild(style)
let popup

button.onclick = () => {
    if (popup && !popup.closed) {
        popup.focus()
        return
    }

    // Open popup
    popup = window.open('https://raw.githubusercontent.com/DorvakOff/DorvakAuthServices/main/DAS/resources/external-scripts/html/popup.html', 'popup', 'width=400,height=450')
    popup.addEventListener('message', (message) => {
        message = message.data
        localStorage.setItem('token', message)
    });

    window.addEventListener("unload", () => {
        popup.close()
    })

    let interval = setInterval(() => {
        if (popup.closed) {
            clearInterval(interval)
            button.disabled = false
        }
    })
}