/**
 * @author ZSS
 * @date 2021/9/16
 * @desc 页面加载
 */

/**
 * 当前页面
 */
function currentPage() {
    let item = localStorage.getItem("smile-office-current-page");
    if (item === null) {
        loadPage("document");
        sideBarActive("document");
        // 加载主页资源
        getDocumentCount();
        getDocumentPage(0, 10, "");
    } else {
        loadPage(item);
        if (item.indexOf("document") >= 0) {
            // 加载主页资源
            sideBarActive("document");
            getDocumentCount();
            getDocumentPage(0, 10, "");
        } else if (item.indexOf("trash") >= 0) {
            // 加载回收站页面
            sideBarActive("trash");
            getTrashDocumentPage(0, 10, "");
        }
    }
}

/**
 * 加载页面
 * @param idName id名
 */
function loadPage(idName) {
    $("#page-load-area").load(idName + ".html");
    localStorage.setItem("smile-office-current-page", idName);
}


/**
 * 设置active
 * @param pageName 页面名称
 */
function sideBarActive(pageName) {
    if (pageName === "document") {
        $("#sidebar-1").attr("class", "active");
        $("#sidebar-2").removeClass("active");
    } else if (pageName === "trash") {
        $("#sidebar-1").removeClass("active");
        $("#sidebar-2").attr("class", "active");
    }
}

// 首页加载
$("#sidebar-1").click(function () {
    sessionStorage.setItem("smile-office-collect", "false");
    loadPage("document");
    sideBarActive("document");
    getDocumentCount();
    getDocumentPage(0, 10, "");
});

// 回收站页加载
$("#sidebar-2").click(function () {
    loadPage("trash");
    sideBarActive("trash");
    getTrashDocumentPage(0, 10, "");
});
