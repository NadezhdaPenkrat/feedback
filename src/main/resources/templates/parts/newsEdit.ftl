<a class="btn btn-primary"
   data-toggle="collapse"
   href="#collapseExample"
   role="button"
   aria-expanded="false"
   aria-controls="collapseExample">

    News editor

</a>

<div class="collapse
        <#if news??>show</#if>"
     id="collapseExample">

    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input
                        type="text"
                        class="form-control ${(textError??)?string('is-invalid', '')}"
                        value="<#if news??>${news.text}</#if>"
                        name="text"
                        placeholder="Введите текст"/>
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input
                        type="text"
                        class="form-control"
                        value="<#if news??>${news.tag}</#if>"
                        name="tag"
                        placeholder="Тэг"/>
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input
                            type="file"
                            name="file"
                            id="customFile"/>
                    <label
                            class="custom-file-label"
                            for="customFile">Choose file
                    </label>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" name="id" value="<#if news??>${news.id}</#if>"/>

            <div class="form-group">
                <button
                        type="submit"
                        class="btn btn-primary">Save news
                </button>
            </div>
        </form>
    </div>
</div>

