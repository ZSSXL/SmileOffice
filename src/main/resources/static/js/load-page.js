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
        } else if (item.indexOf("favorite") >= 0) {
            // 加载收藏页
            sideBarActive("favorite");
        } else if (item.indexOf("trash") >= 0) {
            // 加载首页
            sideBarActive("trash");
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
        $("#sidebar-3").removeClass("active");
    } else if (pageName === "favorite") {
        $("#sidebar-1").removeClass("active");
        $("#sidebar-2").attr("class", "active");
        $("#sidebar-3").removeClass("active");
    } else if (pageName === "trash") {
        $("#sidebar-1").removeClass("active");
        $("#sidebar-2").removeClass("active");
        $("#sidebar-3").attr("class", "active");
    }
}

// 首页加载
$("#sidebar-1").click(function () {
    console.log("load document")
    loadPage("document");
    sideBarActive("document");
    getDocumentCount();
    getDocumentPage(0, 10, "");
});

// 收藏夹页加载
$("#sidebar-2").click(function () {
    console.log("load favorite")
    loadPage("favorite");
    sideBarActive("favorite");
});

// 回收站页加载
$("#sidebar-3").click(function () {
    console.log("load trash")
    loadPage("trash");
    sideBarActive("trash");
});
