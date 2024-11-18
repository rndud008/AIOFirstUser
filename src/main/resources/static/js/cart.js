
const allCheck = (input) => {
    const checkbox = input.parentNode.querySelector('input[type="checkbox"]')

    if (checkbox.checked === true) {
        const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = true;
        })

    } else {
        const checkboxes = document.querySelectorAll('tbody input[type="checkbox"]');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = false;
        })
    }

    sumTotalPriceUpdate();
}

const cartOptionModify = async (button) => {
    const tr = button.closest('tr');

    const checkbox = tr.querySelector('input[type="checkbox"]');

    const requestData = {
        cartId: checkbox.value
    }

    const queryString = new URLSearchParams(requestData).toString();


    popupWindow = window.open(`/user/cart/optionmodify?${queryString}`, '_blank', 'width=600,height=500');

    const checkPopupClosed = setInterval(() => {
        if (popupWindow.closed) {
            clearInterval(checkPopupClosed);
            window.location.href = '/user/cart'
        }
    }, 1000);

}




const cartIncreaseValue = (button) => {
    const tr = button.closest('tr');

    const input = button.parentNode.querySelector("input[type='number']");
    const price = tr.querySelector('.price');
    const totalPrice = tr.querySelector('.totalPrice');
    const point = tr.querySelector('.point');
    const p = tr.querySelector('.option');

    const start = p.textContent.lastIndexOf(" ") + 1;
    const last = p.textContent.lastIndexOf("개")
    const firstText = p.textContent.substring(0, start);

    (parseInt(input.value) >= 1 || isNaN(parseInt(input.value)))
    && (input.value = parseInt(input.value) + 1)
    || (input.value = 1)

    totalPrice.textContent = (parseInt(price.textContent.replaceAll(",", "")) * parseInt(input.value)).toLocaleString()
    point.textContent = (parseInt(parseInt(totalPrice.textContent.replaceAll(",", "")) / 100)).toLocaleString()
    p.textContent = firstText + (parseInt(input.value)).toLocaleString() + "개"
}

const cartDecreaseValue = (button) => {

    const tr = button.closest('tr');

    const input = button.parentNode.querySelector("input[type='number']");
    const price = tr.querySelector('.price');
    const totalPrice = tr.querySelector('.totalPrice');
    const point = tr.querySelector('.point');
    const p = tr.querySelector('.option');

    const start = p.textContent.lastIndexOf(" ") + 1;
    const last = p.textContent.lastIndexOf("개")
    const firstText = p.textContent.substring(0, start);

    (parseInt(input.value) > 1 || isNaN(parseInt(input.value)))
    && (input.value = parseInt(input.value) - 1)
    || (input.value = 1)

    totalPrice.textContent = (parseInt(price.textContent.replaceAll(",", "")) * parseInt(input.value)).toLocaleString()
    point.textContent = (parseInt(parseInt(totalPrice.textContent.replaceAll(",", "")) / 100)).toLocaleString()
    p.textContent = firstText + (parseInt(input.value)).toLocaleString() + "개"
}

const cartInputValueChange = (cartInput) => {
    const tr = cartInput.closest('tr');

    const input = cartInput.parentNode.querySelector("input[type='number']");
    const price = tr.querySelector('.price');
    const totalPrice = tr.querySelector('.totalPrice');
    const point = tr.querySelector('.point');
    const p = tr.querySelector('.option');

    const start = p.textContent.lastIndexOf(" ") + 1;
    const last = p.textContent.lastIndexOf("개")
    const firstText = p.textContent.substring(0, start);

    (parseInt(input.value) < 1 || isNaN(parseInt(input.value)))
    && (input.value = 1)

    totalPrice.textContent = (parseInt(price.textContent.replaceAll(",", "")) * parseInt(input.value)).toLocaleString()
    point.textContent = (parseInt(parseInt(totalPrice.textContent.replaceAll(",", "")) / 100)).toLocaleString()
    p.textContent = firstText + (parseInt(input.value)).toLocaleString() + "개"
}

const addCartItemToWishlist = async (button) => {

    const input = button.parentNode.querySelector("input[type='hidden']")

    console.log(input.value)

    const response = await fetch(`/user/wish/save`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            productVariantId: input.value
        })
    })

    const text = await response.text();

    if (text === "SUCCESS") {
        alert('관심상품 등록 완료')
        button.disabled = true;
        button.textContent = '이미등록된상품'
        button.removeAttribute('onclick');
    } else {
        alert('관심상품 등록 실패')
    }

}

const cartItemDelete = async (button) => {

    const tr = button.closest('tr');
    const inputCheckbox = tr.querySelector('input[type="checkbox"]');
    const cartId = inputCheckbox.value

    const response = await fetch(`/user/cart/delete/${cartId}`, {
        method: 'delete'
    })
    const text = await response.text();

    if (text === "SUCCESS") {
        tr.remove();
    } else {
        alert('잘못된 요청 입니다.')
    }
}

function sumTotalPriceContext(sumTotalPrice, totalDiv, deliveryPrice, keyword) {

    if (sumTotalPrice >= 50000) {

        totalDiv.innerHTML =
            `
                        <span>${keyword} 상품 금액 </span>
                        <span>${sumTotalPrice.toLocaleString()}</span>
                        <span> = </span>
                        <span>결제 예정금액 </span>
                        <span>${sumTotalPrice.toLocaleString()}</span>
                `
    } else {
        totalDiv.innerHTML =
            `
                        <span>${keyword} 상품 금액 </span>
                        <span>${sumTotalPrice.toLocaleString()}</span>
                        <span> + </span>
                        <span>배송료</span>
                        <span> ${deliveryPrice.toLocaleString()}</span>
                        <span> = </span>
                        <span>결제 예정금액 </span>
                        <span>${(sumTotalPrice + deliveryPrice).toLocaleString()}</span>
                    `
    }
}

const sumTotalPriceUpdate = () => {
    const trList = Array.from(document.querySelectorAll('tbody tr'));
    const totalDiv = document.querySelector('#sumTotalPrice');

    const checkedCount = trList.filter((tr, index) => {
        const checkbox = tr.querySelector("input[type='checkbox']")
        return checkbox.checked === true;
    })

    let sumTotalPrice = 0;
    const deliveryPrice = 3000;

    if (checkedCount.length === 0 || checkedCount.length === trList.length) {

        trList && trList.forEach(tr => {
            const totalPrice = tr.querySelector('.totalPrice').textContent.replaceAll(",", "");
            sumTotalPrice += parseInt(totalPrice);
        })

        sumTotalPriceContext(sumTotalPrice, totalDiv, deliveryPrice, "총")

    } else {

        checkedCount.forEach(tr => {
            const totalPrice = tr.querySelector('.totalPrice').textContent.replaceAll(",", "");
            sumTotalPrice += parseInt(totalPrice);
        })

        sumTotalPriceContext(sumTotalPrice, totalDiv, deliveryPrice, "총 선택")

    }

}

const cartAllDelete = async () => {
    const trList = Array.from(document.querySelectorAll('tbody tr'));

    const cartIdList = trList.map(tr => {
        return tr.querySelector("input[type='checkbox']").value;
    })

    if (cartIdList.length === 0) return;

    if (!confirm('장바구니를 비우시겠습니까?')) return;

    const response = await fetch(`/user/cart/delete/allcart`, {
        method: 'DELETE'
    })

    const text = await response.text();

    if (text === "SUCCESS") {
        trList.forEach(tr => {
            tr.remove();
        })
        sumTotalPriceUpdate()
    } else {
        alert('잘못된 접근입니다.');
    }

}

const selectCartDelete = async () => {

    const trList = Array.from(document.querySelectorAll('tbody tr'));

    const checkedList = trList.filter(tr => {
        return tr.querySelector("input[type='checkbox']").checked;
    })

    const cartIdList = checkedList.map(tr => {
        return tr.querySelector("input[type='checkbox']").value;
    })

    if (cartIdList.length === 0) return;

    const items = [];
    cartIdList.forEach(value => {
        items.push({
            cartId: value
        })
    })

    const response = await fetch(`/user/cart/delete/selectcart`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({cartRequestDTOS: items})
    })

    const text = await response.text();

    if (text === "SUCCESS") {
        checkedList.forEach(tr => {
            tr.remove()
        })
    } else {
        alert('잘못된 접근 입니다.')
    }

}

const selectOrder = async () => {

    const trList = Array.from(document.querySelectorAll('tbody tr'));

    const checkedList = trList.filter(tr => {
        return tr.querySelector("input[type='checkbox']").checked;
    })

    if (checkedList.length === 0) return;
    const items = [];

    checkedList.forEach(tr => {
        const text = tr.querySelector("input[type='checkbox']").value + '-' + tr.querySelector("input[type='number']").value
        items.push({
            cartIdAndQuantity: text
        })
    })

    const params = new URLSearchParams();

    items.forEach(item => {
        params.append('cartIdAndQuantity', item.cartIdAndQuantity);
    })

    window.location.href = `/user/order?${params.toString()}`

}

const AllOrder = () => {

    const trList = Array.from(document.querySelectorAll('tbody tr'));

    if (trList.length === 0) return;
    const items = [];

    trList.forEach(tr => {
        console.log(tr)

        const text = tr.querySelector("input[type='checkbox']").value + '-' + tr.querySelector("input[type='number']").value
        console.log(text)
        items.push({
            cartIdAndQuantity: text
        })
    })

    const params = new URLSearchParams();

    items.forEach(item => {
        params.append('cartIdAndQuantity', item.cartIdAndQuantity);
    })

    window.location.href = `/user/order?${params.toString()}`

}