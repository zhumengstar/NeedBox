$(function() {
	var productId = getQueryString('productId');
	var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;

	$.getJSON(productUrl, function(data) {
		if (data.success) {
			var product = data.product;
			console.log(product);
			$('#product-img').attr('src', product.imgAddr);
			$('#product-time').text(new Date(product.lastEditTime).Format("yyyy-MM-dd"));
			$('#product-name').text(product.productName);
			$('#product-desc').text(product.productDesc);
            if (product.normalPrice != null && product.promotionPrice != null) {
                $('#price').show();
                $('#normalPrice').html('<del>' + '￥' + product.normalPrice + '</del>');
                $('#promotionPrice').text('￥' + product.promotionPrice);
            } else if (product.normalPrice != null && product.promotionPrice == null) {
                $('#price').show();
                $('#promotionPrice').text('￥' + product.normalPrice);
            } else if (product.normalPrice == null && product.promotionPrice != null) {
                $('#price').show();
                $('#promotionPrice').text('￥' + product.promotionPrice);
            } else if (product.normalPrice == null && product.promotionPrice == null) {
                $('#price').show();
			}
			var imgListHtml = '';
			product.productImgList.map(function(item, index) {
                imgListHtml += '<div><img src="' + item.imgAddr + '" width="100%"/></div>';
			});
			$('#imgList').html(imgListHtml);
		}
	});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
