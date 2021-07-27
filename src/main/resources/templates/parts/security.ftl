<!-- шаблон , помощник с описанием некоторых переменных assign,
который позволяет определять несколько переменных подряд
прямо в шаблон-->

<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <!-- содержит нашего пользователя, которого мы описываем в базе данных
     и позволит испоьзовать любые методы из него, как getUsername и др.
     -->
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    currentUserId = user.getId()
    >

<#else>
    <#assign
    name = "unknown"
    isAdmin = false
    currentUserId = -1
    >
</#if>
