/**
 * @author ZSS
 * @date 2021/9/16
 * @desc 主页
 */

/**
 * 获取个人信息
 */
function getPersonalInfo() {
    $.ajax({
        url: "/user/info",
        contentType: "application/json;charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                $("#info-username").text(result.data.username);
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}

/**
 * 获取文档统计
 */
function getDocumentCount() {
    $.ajax({
        url: "/doc/count",
        contentType: "application/json;charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                showDocumentCount(result.data);
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}

/**
 * 展示文档统计
 * @param data 统计数据
 */
function showDocumentCount(data) {
    $("#word-count").text(data.word);
    $("#cell-count").text(data.cell);
    $("#slide-count").text(data.slide);
}

// 当前文档页数
let documentPages;
// 当前页
let curDocumentPage;

/**
 * 分页获取文档
 * @param page 当前页
 * @param size 每页大小
 * @param documentType 文档类型 -- word, cell, slide
 */
function getDocumentPage(page, size, documentType) {
    let collect = showCollect();
    let data = {page, size, documentType, collect};
    $.ajax({
        url: "/doc/page",
        contentType: "application/json;charset=utf-8",
        type: "POST",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        dataType: "json",
        data: JSON.stringify(data),
        success: function (result) {
            if (result.status === 0) {
                documentPages = result.data.totalPages;
                curDocumentPage = result.data.number;
                showDocuments(result.data);
                buildPagination(result.data);
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}

/**
 * 是否展示收藏数据
 */
function showCollect() {
    let collect = sessionStorage.getItem("smile-office-collect");
    return collect === null ? false : collect;
}

/**
 * 分页展示文档
 * @param data 文档数据
 */
function showDocuments(data) {
    $("#load-document-area").empty();
    const contents = data.content;
    if (contents.length === 0) {
        $("#no-resources").attr("style", "");
        $("#load-pagination-area").css("display", "none");
    } else {
        $("#no-resources").css("display", "none");
        $("#load-pagination-area").css("display", "inline");
    }
    $.each(contents, function (index, content) {
        let documentType = "word";
        let iconBg = "bg-blue";
        if (content.documentType === "word") {
            documentType = "word";
            iconBg = "bg-blue";
        } else if (content.documentType === "cell") {
            documentType = "excel";
            iconBg = "bg-success";
        } else if (content.documentType === "slide") {
            documentType = "ppt";
            iconBg = "bg-info";
        }

        // pdf类型，单独处理
        let exts = content.documentName.substring(content.documentName.length - 3);
        if (exts === "pdf") {
            documentType = "pdf";
            iconBg = "bg-danger";
        }

        let docNameContentDiv = $("<div class='d-flex align-items-center'></div>")
            .append($("<div></div>").attr("class", "icon-small rounded mr-3 " + iconBg)
                .append($("<i></i>").attr("class", "ri-file-" + documentType + "-line")))
            .append($("<div class='review-doc' style='cursor: pointer;'></div>").attr("doc-id", content.docId)
                .append(content.documentName));

        if (content.collect) {
            docNameContentDiv.append($("<i class='lar la-star ml-2'></i>"));
        }

        let docNameTd = $("<td></td>").append(docNameContentDiv);

        let recentEditTd = $("<td></td>").append(content.updateTime);

        let sizeTd = $("<td></td>").append(content.documentSize == null ? "0.00 KB" : content.documentSize);

        let operationDiv = $("<div class='dropdown-menu dropdown-menu-right'></div>").attr("doc-id", content.docId)
            .append($("<a  class='dropdown-item review-doc' href='#'></a>").attr("doc-id", content.docId)
                .append($("<i class='ri-eye-fill mr-2'></i>").attr("doc-id", content.docId))
                .append("查看"));

        let collectDiv;

        if (content.collect) {
            collectDiv = $("<a  class='dropdown-item cancel-collect-doc' href='#'></a>")
                .append($("<i class='ri-star-fill mr-2'></i>"))
                .append("取消收藏");
        } else {
            collectDiv = $("<a  class='dropdown-item collect-doc' href='#'></a>")
                .append($("<i class='ri-star-fill mr-2'></i>"))
                .append("收藏");
        }

        operationDiv.append(collectDiv);

        let operationTd = $("<td></td>")
            .append($("<div class='dropdown'></div>")
                .append($("<span class='dropdown-toggle' data-toggle='dropdown'></span>")
                    .append($("<i class='ri-more-fill'></i>")))
                .append(operationDiv
                    .append($("<a class='dropdown-item rename-doc' href='#'></a>").attr("doc-name", content.documentName)
                        .append($("<i class='ri-edit-fill mr-2'></i>"))
                        .append("重命名"))
                    .append($("<a  class='dropdown-item delete-doc' href='#'></a>")
                        .append($("<i class='ri-delete-bin-6-fill mr-2'></i>"))
                        .append("删除"))));

        $("<tr></tr>")
            .append(docNameTd)
            .append(recentEditTd)
            .append(sizeTd)
            .append(operationTd)
            .appendTo("#load-document-area");
    });
}

/**
 * 构建分页按钮
 * @param data 分页数据
 */
function buildPagination(data) {
    $("#load-pagination-area").empty();

    let ul = $("<ul class='pagination justify-content-center mt-4'></ul>");

    let prePageLi = $("<li class='page-item'></li>")
        .append($("<a class='page-link' href='#' aria-label='Previous'></a>")
            .append($("<span aria-hidden='true'>&laquo;</span>")));

    // 判断是否首页
    if (data.first) {
        prePageLi.addClass("disabled");
    } else {
        prePageLi.click(function () {
            getDocumentPage(curDocumentPage - 1, 10, "");
        });
    }

    ul.append(prePageLi);

    if (documentPages > 5) {
        if (curDocumentPage > 2) {
            for (i = curDocumentPage - 1; i < curDocumentPage + 2; i++) {
                let numLi = $("<li class='page-item'></li>")
                    .append($("<a class='page-link document-page-jump' style='cursor: pointer;'></a>").append(i + 1));
                if (curDocumentPage === i) {
                    numLi.addClass("active");
                }
                if (documentPages === i) {
                    break;
                }
                ul.append(numLi);
            }
        } else {
            for (i = 0; i < 5; i++) {
                let numLi = $("<li class='page-item'></li>")
                    .append($("<a class='page-link document-page-jump' style='cursor: pointer;'></a>").append(i + 1));
                if (curDocumentPage === i) {
                    numLi.addClass("active");
                }
                ul.append(numLi);
            }
        }
    } else {
        for (i = 0; i < documentPages; i++) {
            let numLi = $("<li class='page-item'></li>")
                .append($("<a class='page-link document-page-jump' style='cursor: pointer;'></a>").append(i + 1));
            if (curDocumentPage === i) {
                numLi.addClass("active");
            }
            ul.append(numLi);
        }
    }

    // 前一页
    let nextPageLi = $("<li class='page-item'></li>")
        .append($("<a class='page-link' href='#' aria-label='Next'></a>")
            .append($("<span aria-hidden='true'>&raquo;</span>")));

    // 判断是否还有上一页， 没有则disabled
    if (data.last) {
        nextPageLi.addClass("disabled");
    } else {
        nextPageLi.click(function () {
            getDocumentPage(curDocumentPage + 1, 10, "");
        });
    }

    ul.append(nextPageLi);

    ul.appendTo("#load-pagination-area");
}

/* 文档分页跳转 */
$(document).on("click", ".document-page-jump", function () {
    let page = $(this).text();
    getDocumentPage(page - 1, 10, "");
});

/**
 * 创建新文档
 */
$(document).on("click", ".create-new", function () {
    let docType = $(this).attr("doc-type");
    console.log("CreateNew: " + docType);
    newDocument(docType);
    window.open("editor", "_blank");
});

/**
 * 创建新文档
 * @param docType 文档类型
 */
function newDocument(docType) {
    $.ajax({
        url: "/doc/create/?fileExts=" + docType,
        contentType: "application/json;charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                // 将查询结果保存到本地存储，传递给新页面
                localStorage.setItem("smile-office-config", JSON.stringify(result.data));
                getDocumentPage(curDocumentPage, 10, "");
                getDocumentCount();
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}

/**
 * 显示上传modal
 */
$("#show-upload-modal").click(function () {
    $("#upload-doc-modal").modal("show");
    console.log("hello upload modal");
});

/**
 * 出发上传input
 */
$(document).on("click", "#upload-document-btn", function () {
    console.log("click upload input")
    $("#upload-input").click();
});

/**
 * 选择了文件触发
 */
function change() {
    let file = document.getElementById("upload-input");
    let docName = file.value.replaceAll("\\", "/");
    $("#upload-doc-name").text(docName.substring(docName.lastIndexOf("/") + 1));
}

$(document).on("click", "#upload-btn", function () {
    let file = $("#upload-input")[0].files[0];
    let formData = new FormData();
    formData.append("document", file);

    $.ajax({
        url: "/doc/upload",
        type: "POST",
        cache: false,
        processData: false,
        contentType: false,
        dataType: "json",
        mimeType: "multipart/form-data",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        data: formData,
        success: function (result) {
            if (result.status === 0) {
                Notiflix.Notify.Info("上传成功");
                $("#upload-doc-modal").modal("hide");
                $("#upload-doc-name").text("");
                getDocumentPage(curDocumentPage, 10, "");
                getDocumentCount();
            } else {
                Notiflix.Notify.Failure(result.msg);
            }
        }
    });
})

/**
 * 修改密码
 */
$(document).on("click", "#modify-password-btn", function () {
    let oldPassword = $("#old-password").val();
    let newPassword = $("#new-password").val();
    let confirmPassword = $("#confirm-password").val();
    if (oldPassword === "") {
        Notiflix.Notify.Warning("旧密码不能为空!");
        return false;
    }
    if (newPassword === "") {
        Notiflix.Notify.Warning("新密码不能为空!");
        return false;
    }
    if (confirmPassword === "") {
        Notiflix.Notify.Warning("确认密码不能为空!");
        return false;
    }

    if (checkPassword(newPassword, confirmPassword)) {
        let data = {oldPassword, newPassword};
        modify(data);
    }
});

/**
 * 校验密码
 */
function checkPassword(password, confirmPassword) {
    if (password !== confirmPassword) {
        Notiflix.Notify.Warning("确认密码不正确");
        $("#confirm-password").val("");
        $("#new-password").val("").focus();
        return false;
    } else {
        return true;
    }
}

/* 修改密码 */
function modify(data) {
    $.ajax({
        url: "/user/modify",
        contentType: "application/json;charset=utf-8",
        type: "POST",
        dataType: "json",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        data: JSON.stringify(data),
        success: function (result) {
            analysisResult(result);
        }
    });
}

/* 解析数据 */
function analysisResult(result) {
    if (result.status === 0) {
        Notiflix.Notify.Success("密码修改成功，重新登录");
        window.location.href = "/signin";
    } else {
        Notiflix.Notify.Failure("密码修改失败: " + result.msg);
        $("#confirm-password").val("");
        $("#new-password").val("").focus();
    }
}