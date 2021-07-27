<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>

<#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>

    <div class="alert alert-danger"
         role="alert">
        ${Session.SPRING_SECURITY_LAST_EXCEPTION.news}
    </div>
</#if>

<#if news??>
    <div class="alert alert-${messageType}"
         role="alert">
        ${news}
    </div>
</#if>

<@l.login "/login" false/>

</@c.page>
