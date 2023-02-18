
let css = `
body {
    margin: 0;
    padding: 0;
    font-family: sans-serif;
    background-color: #f5f5f5;
}

form {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
    box-sizing: border-box;
    padding: 20px;
}

input {
    width: 300px;
    height: 40px;
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 12px 20px;
    margin: 8px 0;
    box-sizing: border-box;
}

#submit {
    width: 300px;
    height: 40px;
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

#submit:hover {
    background-color: #45a049;
}

#error {
    color: red;
    font-size: 12px;
    margin-top: 10px;
    text-align: center;
    width: 300px;
    height: 20px;
    background-color: #f8d7da;
    border: 1px solid #f5c6cb;
    border-radius: 4px;
    padding: 12px 20px;
    margin: 8px 0;
}

#submit:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}

#submit:disabled:hover {
    background-color: #ccc;
}
`

let style = document.createElement('style')

if (style.styleSheet) {
    style.styleSheet.cssText = css
} else {
    style.appendChild(document.createTextNode(css))
}

document.getElementsByTagName('head')[0].appendChild(style)

function submitForm() {
    let url = "http://localhost:8080/user/login";
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let button = document.getElementById("submit");
    let error = document.getElementById("error");

    if (!email || !password) {
        error.innerHTML = "Please fill in all fields"
        error.hidden = false
        return
    }

    error.hidden = true
    button.disabled = true

    let data = {
        email: email,
        password: password
    }
    let params = {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    fetch(url, params)
        .then(response => {
            if (!response.ok) {
                throw Error(response.statusText);
            }
            return response
        })
        .catch(() => {
            button.disabled = false
            error.innerHTML = "Invalid email or password"
            error.hidden = false
        })
        .then(response => response ? response.text() : null)
        .then(data => {
            if (data) {
                window.postMessage(data)
                window.close()
            }
        })
}