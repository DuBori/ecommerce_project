// 파일 입력(input) 요소에 change 이벤트 리스너 추가
$(function () {
    $('#imageInput').on('change', function(event) {
        let path = $(this).data('path');
        var file = event.target.files[0]; // 선택된 파일 가져오기
        var reader = new FileReader();

        reader.onload = function(event) {
            var base64Data = event.target.result; // 파일의 내용을 Base64 데이터 URL로 가져옴
            var image = $('<img>').attr('src', base64Data);

            uploadImage(file, path);

            if ($('#imageContainer img').length > 0) {
                $('#imageContainer img').remove();
            }
            $('#imageContainer').html(image);
        };

        reader.readAsDataURL(file);
    });

    function uploadImage(file, path) {
        var formData = new FormData();
        formData.append('file', file);
        formData.append('path', path);

        $.ajax({
            url: '/image/upload',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(data) {
                $('#filePath').val(data.body);
            },
            error: function(xhr, status, error) {
                console.error('Error uploading image:', error);
            }
        });
    }
})
