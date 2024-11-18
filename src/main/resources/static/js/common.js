const getAccessToken = async () =>{
    const cookies = document.cookie.split('; ')
    let returnValue = '';

    cookies.forEach(cookie =>{
        const [name, value] = cookie.split('=')

        if (name === 'accessToken'){
            returnValue = value;
        }
    })

    return returnValue;
}

const historyBack = ()=>{
    window.history.back();
}

