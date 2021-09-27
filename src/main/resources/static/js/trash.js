/**
 * @author ZSS
 * @date 2021/9/27
 * @desc 回收站相关操作
 */

// 当前文档页数
let trashDocumentPages;
// 当前页
let trashCurDocumentPage;

/**
 * 分页获取文档
 * @param page 当前页
 * @param size 每页大小
 * @param documentType 文档类型 -- word, cell, slide
 */
function getTrashDocumentPage(page, size, documentType) {
    let data = {page, size, documentType};
    $.ajax({
        url: "/recycle/page",
        contentType: "application/json;charset=utf-8",
        type: "POST",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        dataType: "json",
        data: JSON.stringify(data),
        success: function (result) {
            if (result.status === 0) {
                trashDocumentPages = result.data.totalPages;
                trashCurDocumentPage = result.data.number;
                showTrashDocuments(result.data);
                buildTrashPagination(result.data);
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}

/**
 * 分页展示文档
 * @param data 文档数据
 */
function showTrashDocuments(data) {
    $("#load-trash-document-area").empty();
    const contents = data.content;
    if (contents.length === 0) {
        $("#no-trash-resources").attr("style", "");
        $("#load-trash-pagination-area").css("display", "none");
    } else {
        $("#no-trash-resources").css("display", "none");
        $("#load-trash-pagination-area").css("display", "inline");
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

        let docNameTd = $("<td></td>").append($("<div class='d-flex align-items-center'></div>")
            .append($("<div></div>").attr("class", "icon-small rounded mr-3 " + iconBg)
                .append($("<i></i>").attr("class", "ri-file-" + documentType + "-line")))
            .append($("<div class='review-doc' style='cursor: pointer;'></div>").attr("doc-id", content.docId)
                .append(content.documentName)));

        let recentEditTd = $("<td></td>").append(content.createTime + " 天");

        let sizeTd = $("<td></td>").append(content.documentSize == null ? "0.00 KB" : content.documentSize);

        let operationTd = $("<td></td>")
            .append($("<div class='dropdown'></div>")
                .append($("<span class='dropdown-toggle' data-toggle='dropdown'></span>")
                    .append($("<i class='ri-more-fill'></i>")))
                .append($("<div class='dropdown-menu dropdown-menu-right'></div>").attr("doc-id", content.docId)
                    .append($("<a  class='dropdown-item reduce-doc' href='#'></a>")
                        .append($("<i class='ri-recycle-fill mr-2'></i>"))
                        .append("还原"))
                    .append($("<a  class='dropdown-item delete-trash-doc' href='#'></a>")
                        .append($("<i class='ri-delete-bin-6-fill mr-2'></i>"))
                        .append("删除"))));

        $("<tr></tr>")
            .append(docNameTd)
            .append(recentEditTd)
            .append(sizeTd)
            .append(operationTd)
            .appendTo("#load-trash-document-area");
    });
}

/**
 * 构建分页按钮
 * @param data 分页数据
 */
function buildTrashPagination(data) {
    $("#load-trash-pagination-area").empty();

    let ul = $("<ul class='pagination justify-content-center mt-4'></ul>");

    let prePageLi = $("<li class='page-item'></li>")
        .append($("<a class='page-link' href='#' aria-label='Previous'></a>")
            .append($("<span aria-hidden='true'>&laquo;</span>")));

    // 判断是否首页
    if (data.first) {
        prePageLi.addClass("disabled");
    } else {
        prePageLi.click(function () {
            getTrashDocumentPage(trashCurDocumentPage - 1, 10, "");
        });
    }

    ul.append(prePageLi);

    if (trashDocumentPages > 5) {
        if (trashCurDocumentPage > 2) {
            for (i = trashCurDocumentPage - 1; i < trashCurDocumentPage + 2; i++) {
                let numLi = $("<li class='page-item'></li>")
                    .append($("<a class='page-link trash-document-page-jump' style='cursor: pointer;'></a>").append(i + 1));
                if (trashCurDocumentPage === i) {
                    numLi.addClass("active");
                }
                if (trashDocumentPages === i) {
                    break;
                }
                ul.append(numLi);
            }
        } else {
            for (i = 0; i < 5; i++) {
                let numLi = $("<li class='page-item'></li>")
                    .append($("<a class='page-link trash-document-page-jump' style='cursor: pointer;'></a>").append(i + 1));
                if (trashCurDocumentPage === i) {
                    numLi.addClass("active");
                }
                ul.append(numLi);
            }
        }
    } else {
        for (i = 0; i < trashDocumentPages; i++) {
            let numLi = $("<li class='page-item'></li>")
                .append($("<a class='page-link trash-document-page-jump' style='cursor: pointer;'></a>").append(i + 1));
            if (trashCurDocumentPage === i) {
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
            getTrashDocumentPage(trashCurDocumentPage + 1, 10, "");
        });
    }

    ul.append(nextPageLi);

    ul.appendTo("#load-trash-pagination-area");
}

/* 文档分页跳转 */
$(document).on("click", ".trash-document-page-jump", function () {
    let page = $(this).text();
    getTrashDocumentPage(page - 1, 10, "");
});

/**
 * 从回收站还原文档
 */
$(document).on("click", ".reduce-doc", function () {
    let docId = $(this).parent().attr("doc-id");
    $.ajax({
        url: "/recycle/reduce/" + docId,
        contentType: "application/json;charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                Notiflix.Notify.Success(result.msg);
                getTrashDocumentPage(trashCurDocumentPage, 10, "")
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
});

/**
 * 从回收站彻底删除文档
 */
$(document).on("click", ".delete-trash-doc", function () {
    let docId = $(this).parent().attr("doc-id");
    Notiflix.Confirm.Show(
        "警告!",
        "删除无法再恢复",
        "确定",
        "取消",
        function () {
            deleteDocFromRecycleBin(docId);
        }
    )
});

/**
 * 从回收站强制删除文档
 * @param docId 文档Id
 */
function deleteDocFromRecycleBin(docId) {
    $.ajax({
        url: "/recycle/delete/force/" + docId,
        contentType: "application/json;charset=utf-8",
        type: "GET",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("smile_token", token);
        },
        success: function (result) {
            if (result.status === 0) {
                Notiflix.Notify.Success(result.msg);
                getTrashDocumentPage(trashCurDocumentPage, 10, "")
            } else {
                Notiflix.Notify.Warning(result.msg);
            }
        }
    });
}