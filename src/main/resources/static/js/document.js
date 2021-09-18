/**
 * @author ZSS
 * @date 2021/9/16
 * @desc 文档相关操作
 */

/**
 * 查看文档
 */
$(document).on("click", ".review-doc", function () {
    let docId = $(this).attr("doc-id");
    reviewDocument(docId);
    window.open("editor", "_blank");
});

/**
 * 查看文件
 * @param docId
 */
function reviewDocument(docId) {
    $.ajax({
        url: "/doc/review/" + docId,
        contentType: "application/json;charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                // 将查询结果保存到本地存储，传递给新页面
                localStorage.setItem("smile-office-config", JSON.stringify(result.data));
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}


/**
 * 删除文档
 */
$(document).on("click", ".delete-doc", function () {
    let docId = $(this).parent().attr("doc-id");
    deleteDocument(docId);
});

/**
 * 删除文档
 * @param docId 文档Id
 */
function deleteDocument(docId) {
    $.ajax({
        url: "/doc/delete/" + docId,
        contentType: "application/json;charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                Notiflix.Notify.Success(result.msg);
                getDocumentPage(curDocumentPage, 10, "");
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}

/**
 * 文档重命名
 */
$(document).on("click", ".rename-doc", function () {
    let docId = $(this).parent().attr("doc-id");
    let docName = $(this).attr("doc-name");
    // 获取文件拓展名
    let docExtension = docName.substring(docName.lastIndexOf("."));

    $("#rename-btn").attr("doc-id", docId)
    $("#old-document-name").val(docName);
    $("#doc-extension").text(docExtension);

    $("#rename-doc-modal").modal("show");
})

$(document).on("click", "#rename-btn", function () {
    let docId = $(this).attr("doc-id");
    let documentName = $("#old-document-name").val();
    let newDocumentName = $("#new-document-name").val();
    let docExts = $("#doc-extension").text();
    if (newDocumentName === ""){
        Notiflix.Notify.Warning("新文件名不能为空");
        $("#new-document-name").focus();
    } else {
        newDocumentName = newDocumentName + docExts;
        let data = {docId, documentName, newDocumentName};
        $.ajax({
            url: "/doc/rename",
            contentType: "application/json;charset=utf-8",
            type: "POST",
            beforeSend: function (XMLHttpRequest) {
                XMLHttpRequest.setRequestHeader("smile_token", token);
            },
            dataType: "json",
            data: JSON.stringify(data),
            success: function (result) {
                if (result.status === 0) {
                    Notiflix.Notify.Success(result.msg);
                    $("#new-document-name").val("");
                    getDocumentPage(curDocumentPage, 10, "");
                    $("#rename-doc-modal").modal("hide");
                } else {
                    Notiflix.Notify.Warning(result.msg);
                }
            }
        });
    }
})