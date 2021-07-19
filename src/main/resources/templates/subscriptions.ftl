<#import "parts/common.ftl" as c>

<!--   -->

<@c.page>
    <!--   -->
    <h3>${userChannel.username}</h3>
    <!--  отмечается кто, подписчики или подписки  -->
    <div>${type}</div>

    <ul class="list-group">
        <!--  выводится список пользователей -->
        <#list users as user>
            <li class="list-group-item">
                <!--  ссылка на страницу пользователя, которую отображаем -->
                <a href="/user-tidings/${user.id}">
                    ${user.getUsername()}
                </a>
            </li>
        </#list>
    </ul>

</@c.page>
