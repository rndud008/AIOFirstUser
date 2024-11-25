const updateSizePrice = () => {
    const colorSizePrice = product?.colorSizePrice;

    const color = document.getElementById('colorSelect').value;
    const sizeSelect = document.getElementById('sizeSelect');

    sizeSelect.innerHTML = '';

    if (color && colorSizePrice[color]) {
        const sizes = colorSizePrice[color];
        const defaultOption = document.createElement('option');
        defaultOption.text = "옵션 선택";
        defaultOption.value = "";
        sizeSelect.add(defaultOption);

        Object.entries(sizes).forEach(([size, price]) => {
            const option = document.createElement('option');
            option.text = size;
            option.value = color + ',' + size + ',' + price;
            sizeSelect.add(option);
        })

    }
}

const productAdd = () => {

    const value = document.getElementById('sizeSelect').value;

    const color = value.split(',')[0]
    const size = value.split(',')[1]
    const price = value.split(',')[2]

    if (!document.getElementById(`${color}-${size}`) && value) {
        const list = document.getElementById('productAddList');
        const item = document.createElement('li');

        const index = list.querySelectorAll('li').length;
        console.log()

        const sellPrice = parseInt(product.sellPrice.substring(0, product.sellPrice.indexOf("원")).replace(",", "")) + parseInt(price);

        console.log(sellPrice)

        item.setAttribute('id', `${color}-${size}`)

        item.innerHTML = `
                <div>
                    <span >${color}, ${size}</span> <button onclick="deleteItem(this)"><i class="fa-solid fa-xmark"></i></button>
                </div>
                <div>
                    <input type="hidden" name="productId[${index}]" value="${product.id}" />
                    <input type="hidden" name="colorSize[${index}]" value="${color}-${size}" />
                    <input type="number" name="quantity[${index}]" value="1" oninput="changeValue(this,${sellPrice})"/>
                    <button onclick="increaseValue(this,${sellPrice})"><i class="fa-solid fa-plus"></i></button>
                    <button onclick="decreaseValue(this,${sellPrice})"><i class="fa-solid fa-minus"></i></button>
                    <span class="price">${sellPrice.toLocaleString()}원</span>
                </div>
                `
        list.appendChild(item)
    }
    updateTotalPrice()
}

const deleteItem = (button) => {
    const deleteItem = button.closest('li');
    if (deleteItem) {
        deleteItem.remove()
    }
    updateTotalPrice()
}

const increaseValue = (button, sellPrice) => {
    const inputItem = button.parentNode.querySelector('input[type="number"]');
    const spanItem = button.parentNode.querySelector('span');

    inputItem.value < 0 && (inputItem.value = 1)

    inputItem
    && (inputItem.value = parseInt(inputItem.value) + 1)
    && (spanItem.textContent = (parseInt(sellPrice) * inputItem.value).toLocaleString() + '원');

    updateTotalPrice()

}

const decreaseValue = (button, sellPrice) => {
    const inputItem = button.parentNode.querySelector('input[type="number"]');
    const spanItem = button.parentNode.querySelector('span');

    inputItem.value <= 0 && (inputItem.value = 1)

    inputItem
    && inputItem.value > 1
    && (inputItem.value = parseInt(inputItem.value) - 1)
    && (spanItem.textContent = (parseInt(sellPrice) * inputItem.value).toLocaleString() + '원') ||
    inputItem
    && inputItem.value === 1
    && (spanItem.textContent = (parseInt(sellPrice) * inputItem.value).toLocaleString() + '원');

    updateTotalPrice()
}

const changeValue = (input, sellPrice) => {

    const inputItem = input.parentNode.querySelector('input[type="number"]');
    inputItem.value <= 0 && (inputItem.value = 1);

    const spanItem = input.parentNode.querySelector('span');

    inputItem
    && (spanItem.textContent = (parseInt(sellPrice) * inputItem.value).toLocaleString() + '원');
    updateTotalPrice()
}

const updateTotalPrice = () => {
    const result = document.getElementsByClassName('price');

    const priceList = Array.from(result).map(span => span.textContent.replace("원", ""))

    const totalPrice = priceList.reduce((total, currentValue) => {
        return parseInt(total) + parseInt(currentValue.replace(/,/g, ""));
    }, 0)

    const totalItem = document.getElementById('totalPrice')

    totalItem.textContent = '총 상품 금액 : ' + totalPrice.toLocaleString() + '원';

}

let slideCurrentIndex = 0;
const slide = (direction) => {

    const wrapper = document.querySelector('.sliderWrapper');
    const items = document.querySelectorAll('.sliderItem');
    const totalItems = items.length;
    const maxVisibleItems = 3;

    if (totalItems > maxVisibleItems) {

        slideCurrentIndex += direction;

        if (slideCurrentIndex < 0) {
            slideCurrentIndex = 0;
        } else if (slideCurrentIndex > totalItems - maxVisibleItems) {
            slideCurrentIndex = totalItems - maxVisibleItems;
        }

    }

    const translateXValue = -(slideCurrentIndex * (items[0].offsetWidth + 10));
    wrapper.style.transform = `translateX(${translateXValue}px)`;

}

const loginValid  = async () =>{
    const accessToken = await getAccessToken();

    if(accessToken === ''){
        return '로그인이 필요합니다.';
    }

    return '';
}

const productValid = async () =>{
    if(document.querySelectorAll('#productAddList li').length < 1){
        return '상품을 추가해주세요.';
    }

    return '';
}



const cartPost = async () => {

    let msg = await loginValid();

    if( msg !== ''){
        alert(msg)
        return;
    }

    msg = await productValid();

    if(msg !== ''){
        alert(msg)
        return;
    }

    const cartItems = [];

    document.querySelectorAll('#productAddList li').forEach((item, index) => {

        const productId = item.querySelector(`input[name = "productId[${index}]"]`).value;
        const colorSize = item.querySelector(`input[name="colorSize[${index}]"]`).value;
        const quantity = item.querySelector(`input[name="quantity[${index}]"]`).value;

        cartItems.push({
            productId: productId,
            colorSize: colorSize,
            quantity: quantity
        });
    });

    const response = await fetch('/user/cart/save', {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({cartRequestDTOS: cartItems})
    })

    if (response.status === 200) {
        const modal = document.createElement('div');
        modal.setAttribute('id', 'modal')
        modal.onclick = () => modalClose(event);

        modal.style.display = "block";

        modal.innerHTML = `
            <div class="cartModalContent">
                <span> 상품이 장바구니에 담겼습니다.</span>
                <div>
                    <button onclick="buttonModalClose()"> 쇼핑 계속하기</button>
                    <a href="/user/cart"> 장바구니 확인</a>
                </div>
            </div>
`
        const productDetailWrapper = document.getElementById('productDetailWrapper');
        productDetailWrapper.insertAdjacentElement('afterend', modal)
    }

}


const orderPage = async () =>{
    let msg = await loginValid();

    if( msg !== ''){
        alert(msg)
        return;
    }

    msg = await productValid();

    if(msg !== ''){
        alert(msg)
        return;
    }


    const productAddList = Array.from(document.getElementById('productAddList').querySelectorAll('li'));

    const items=[];

    productAddList.forEach(li =>{
        let item ={
            productId : 0,
            size : '',
            color : '',
            quantity : 0
        }

        li.querySelectorAll('input').forEach(input =>{
            console.log(input.name,input.value)

            if (input.name.includes('productId')){
                item.productId = input.value;
            }else if (input.name.includes('colorSize')){
                item.color = input.value.split('-')[0];
                item.size = input.value.split('-')[1];
            }else if(input.name.includes('quantity')){
                item.quantity = input.value
            }

        })

        items.push(item);
    })

    console.log(items)

    const response = await fetch(`/user/order`,{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify(items)
    })

    if (response.ok){
        const data = await response.text();
        console.log(data)
        document.open()
        document.write(data)
        document.close()

        accessTokenCheck();
        subCategoryNameCheck();
        window.history.pushState({},'','/user/order')
    }



}

const modalClose = (event) => {

    const modal = document.getElementById('modal');
    const modalContent = document.getElementById('modal-content');

    if (event.target === modalContent) {
        event.stopPropagation();
        return;
    }

    if (event.target === modal) {
        modal.remove();
    }

}

const buttonModalClose = () => {
    const modal = document.getElementById('modal');
    modal.remove();
}

const productFilter = async (span) =>{
    const params = window.location.href.substring(window.location.href.indexOf('?')+1).split('&');
    console.log(params)
    console.log(span.textContent.replaceAll(" ","").trim().toLowerCase())
    let item;
    let code =null;
    let decode = null;
    const filter = span.textContent.replaceAll(" ","").trim().toLowerCase();

    params.forEach(param =>{
        if (param.split('=')[0] === 'code'){
            code=param.split('=')[1]
        } else if (param.split('=')[0] === 'decode'){
            decode=param.split('=')[1]
        }
    })

    item = {
        code,
        decode,
        filter
    }

    console.log(item)

    const response = await fetch('/api/product/category',{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify(item)
    })

    console.log(await response)

    if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`);
        const errorText = await response.text(); // 응답 본문 확인
        console.error(errorText); // HTML 문서 내용이 반환될 수 있음
        return;
    }

    const data = await response.json();
    console.log(data)

    const productItemsDiv = document.getElementsByClassName('productItems').item(0);

    let html='';
    data.forEach(item =>{
        html +=
            `
                <div class="product-item" >
                <a href='/product/detail/${item.id}'>
                    <img src='${item.productImgFileNames}'>
                    <span >${item.productName}</span>
                </a>
                <span class="size">${item.size}</span>
                <span class="consumerPrice" >${item.consumerPrice}</span>
                <span class="sellPrice" >${item.sellPrice}</span>
            </div>
            `
    })
    productItemsDiv.innerHTML = html;


}