
<!--  macro  принимать некоторые аргументы ,
прикручивать на разные страницы,
для разных списков событий
и переменные, которые будут приходить в этот блок кода,
могут называться по-разному  -->

<#macro pager url page> <!--  принимает параметр страницы url и значение page ,
    которое возвращается из репозитория -->

    <#if page.getTotalPages() gt 7>  <!-- отображать, когда  общее количество закладок страниц больше чем 7-->

        <!-- 1) общее количество страниц totalPages -->
        <!-- 2) определим на какаой странице находимся pageNumber-->
        <!-- 3) head начальная страница -->
        <!-- 4) bodyBefore две страницы до текущей    -->
        <!-- 5) bodyAfter две страницы до текущей -->
        <!-- 6) tail  последняя страница     -->
        <#assign

        totalPages = page.getTotalPages()
        pageNumber = page.getNumber() + 1

        head = (pageNumber > 4)?then([1, -1], [1, 2, 3])
        tail = (pageNumber < totalPages - 3)?then([-1, totalPages], [totalPages - 2, totalPages - 1, totalPages])
        bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1)?then([pageNumber - 2, pageNumber - 1], [])
        bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3)?then([pageNumber + 1, pageNumber + 2], [])

        body = head + bodyBefore + (pageNumber > 3 && pageNumber < totalPages - 2)?then([pageNumber], []) + bodyAfter + tail
        >
    <#else>
        <!-- отображать количество страниц, которое имеется-->
        <#assign body = 1..page.getTotalPages()>
    </#if>


    <div class="mt-3">
        <ul class="pagination">
            <li class="page-item disabled">
                <a class="page-link"
                   href="#"
                   tabindex="-1">
                    Страницы
                </a>
            </li>

            <!--синтаксис диапозонов freemarker-->
            <#list body as p>
                <!--если текущая страница совпадает с которой пытаемся отобразить  -->
                <#if (p - 1) == page.getNumber()>
                    <li class="page-item active">
                        <!-- показываем  номер страницы -->
                        <a class="page-link" href="#" tabindex="-1">${p}</a>
                    </li>
                <#elseif p == -1>
                    <li class="page-item disabled">
                        <!-- показываем  многоточие ,вместо номера страницы -->
                        <a class="page-link" href="#" tabindex="-1">...</a>
                    </li>
                <#else>
                    <li class="page-item">
                        <!-- показываем  номер страницы -->
                        <a class="page-link" href="${url}?page=${p - 1}&size=${page.getSize()}" tabindex="-1">${p}</a>
                    </li>
                </#if>
            </#list>
        </ul>


        <ul class="pagination">
            <li class="page-item disabled">
                <a class="page-link"
                   href="#"
                   tabindex="-1">
                    Элементов на странице
                </a>
            </li>


            <!--отображать на одной страничке по n количеству элементов -->
            <#list [5, 10, 25, 50] as c>

                <#if c == page.getSize()>
                    <li class="page-item active">
                        <a class="page-link"
                           href="#"
                           tabindex="-1">
                            ${c}
                        </a>
                    </li>

                <#else>
                    <li class="page-item">
                        <a class="page-link"
                           href="${url}?page=${page.getNumber()}&size=${c}"
                           tabindex="-1">
                            ${c}
                        </a>
                    </li>
                </#if>

            </#list>

        </ul>

    </div>

</#macro>
