<#import "parts/common.ftl" as c>

<@c.page>
    <!--  выводится имя пользователя -->
    <h5>${username}</h5>
    <!--  если существует -->
    ${news?ifExists}
    <!-- форма будет отправляться на тот же адрес с которого пришла , методом  post -->
    <form method="post">
        <!-- заполняется пароль  -->
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input
                        type="password"
                        name="password"
                        class="form-control"
                        placeholder="Password"
                />
            </div>
        </div>
        <!--  в случае, если  Email не указан, будет отображаться пустая строка -->
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email:</label>
            <div class="col-sm-6">
                <input
                        type="email"
                        name="email"
                        class="form-control"
                        placeholder="some@some.com"
                        value="${email!    '  '  }"
                />
            </div>
        </div>

        <input
                type="hidden"
                name="_csrf"
                value="${_csrf.token}"
        />
        <!--  подпишим кнопку сохранить -->
        <button
                class="btn btn-primary"
                type="submit">
            Save
        </button>

    </form>

</@c.page>
