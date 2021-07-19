<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>

    <div class="mb-1">Add new user</div>
    <!-- проверка,нет ли сообщений об ошибках (  падение не произойдет )-->
    ${news?ifExists}
    <@l.login "/registration" true />

</@c.page>
