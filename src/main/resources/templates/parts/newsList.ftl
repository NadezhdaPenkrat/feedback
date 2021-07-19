<#include "security.ftl">
<#import "pager.ftl" as p>

<@p.pager url page /> <!-- постраничная раскладка сверху экрана-->

<div
        class="card-columns"
        id="news-list">

    <#list page.content as news>

        <div
                class="card my-3"
                data-id="${news.id}">

            <#if news.filename??>
                <img src="/img/${news.filename}"
                     class="card-img-top"/>
            </#if>

            <div
                    class="m-2">
                <span>${news.text}</span><br/>
                <i>#${news.tag}</i>
            </div>

            <div class="card-footer text-muted">
                <a href="/user-tidings/${news.author.id}">${news.authorName}</a>
                <#if news.author.id == currentUserId>
                    <a class="btn btn-primary"
                       href="/user-tidings/${news.author.id}?news=${news.id}">
                        Edit
                    </a>
                </#if>
            </div>

        </div>

    <#else>
        No news
    </#list>

</div>

<@p.pager url page /> <!-- постраничная раскладка  снизу экрана-->
