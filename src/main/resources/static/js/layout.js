

document.addEventListener("DOMContentLoaded", () => {
    accessTokenCheck();
    subCategoryNameCheck();
})

function accessTokenCheck() {
    const cookies = document.cookie.split(";");

    cookies.forEach(cookie => {
        const [name, value] = cookie.split('=')

        if (name === "accessToken") {
            const loginHeader = document.getElementById('loginHeader');
            loginHeader.innerHTML =
                `
                     <li style="margin-left: 10px" ><a onclick="logout()">로그아웃</a></li>
                    <li style="margin-left: 10px" ><a href="/user/cart">CART</a></li>
                    <li style="margin-left: 10px; margin-right: 10px" ><a href="/user/mypage">MYPAGE</a></li>
                    `
        }

    })
}

function subCategoryNameCheck() {
    const currentUrl = window.location.href;
    const params = currentUrl.substring(currentUrl.indexOf("?"))
    const subCategoryCheck = params.split("&").length > 1 && params.indexOf("code") > -1 && params.indexOf("decode") > -1
    if (subCategoryCheck) {

        Array.from(document.getElementsByClassName('subCategoryName')).forEach(item => {

            if (item.href.substring(item.href.indexOf("?")) === params) {
                item.classList.add("on")
            }
        })

    }
}

const topScroll = () =>{
    window.scrollTo(0,0)
}

const enterCheck = (event)=>{
    if(event.key === 'Enter'){
        searchProduct();
    }
}

const searchProduct = () =>{
    const search = document.getElementById('searchParam').value
    window.location.href = `/product/search?search=${search}`
}

const logout = async () => {

    const response = await fetch("/user/logout", {
        method: "POST"
    })

    if (response.status === 200) {
        window.location.href = "/"
    }

}

const menuBar = () => {
    const allCategory = document.getElementById('allCategory');
    const menu = document.getElementById('menu')

    if (allCategory.classList.contains('on')) {
        allCategory.classList.remove('on');
        menu.innerHTML = `<i class="fa-solid fa-bars"></i>`
    } else {
        allCategory.classList.add('on');
        menu.innerHTML = `<i class="fa-solid fa-xmark"></i>`
    }

}