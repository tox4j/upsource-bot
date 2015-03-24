<#ftl strip_whitespace=true>
<#import '/ftl/ui.ftl' as ui>
<#-- @ftlvariable name="" type="im.tox.upsourcebot.views.users.NewUserView" -->
<#escape name as name?html>
    <@ui.page "New user">
    <div class="jumbotron">
        <div class="row">
            <div class="new-user">
                <p>Create a new user</p>

                <form action="users/new" method="post">
                    <#if nameEmpty || nameTaken>
                        <div class="form-group has-error">
                            <label for="username">User name</label>
                            <input type="text" class="form-control" id="username" name="username">
                        <span class="help-block">
                            <#if nameEmpty>
                                Name can't be empty
                            <#else>
                                Name is taken
                            </#if>
                        </span>
                        </div>
                    <#else>
                        <div class="form-group">
                            <label for="username">User name</label>
                            <input type="text" class="form-control" id="username" name="username">
                        </div>
                    </#if>
                    <#if passwordEmpty>
                        <div class="form-group has-error">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password"
                                   name="password">
                            <span class="help-block">Password can't be empty</span>
                        </div>
                    <#else>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password"
                                   name="password">
                        </div>
                    </#if>
                    <#if passwordMismatch>
                        <div class="form-group has-error">
                            <label for="password-repeat">Repeat password</label>
                            <input type="password" class="form-control" id="password-repeat"
                                   name="password-repeat">
                            <span class="help-block">Passwords don't match</span>
                        </div>
                    <#else>
                        <div class="form-group">
                            <label for="password-repeat">Repeat password</label>
                            <input type="password" class="form-control" id="password-repeat"
                                   name="password-repeat">
                        </div>
                    </#if>
                    <button type="submit" class="btn btn-primary"><span
                            class="glyphicon glyphicon-save"></span> Create user</button>
                </form>
            </div>
        </div>
    </div>
    </@ui.page>
</#escape>