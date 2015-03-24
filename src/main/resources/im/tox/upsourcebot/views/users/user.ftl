<#ftl strip_whitespace=true>
<#import '/ftl/ui.ftl' as ui>
<#-- @ftlvariable name="" type="im.tox.upsourcebot.views.users.UserView" -->
<#escape name as name?html>
    <@ui.page "Show user">
    <div class="jumbotron">
        <div class="row">
            <#if user??>
                <div class="row">
                    <p>User: ${user.name}</p>

                    <form action="users/${user.id}" method="post">
                        <#if passwordIncorrect>
                            <div class="form-group has-error">
                                <div class="form-group has-error">
                                    <label for="current">Current password</label>
                                    <input type="password" class="form-control" id="current"
                                           name="current">
                                    <span class="help-block">Password is incorrect</span>
                                </div>
                            </div>
                        <#else>
                            <div class="form-group">
                                <label for="current">Current password</label>
                                <input type="password" class="form-control" id="current"
                                       name="current">
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
                                class="glyphicon glyphicon-pencil"></span> Change password
                        </button>
                    </form>
                </div>
                <p>

                <div class="row">
                    <form action="users/delete/${user.id}" method="post">
                        <button type="submit" class="btn btn-danger"><span
                                class="glyphicon glyphicon-trash"></span> Delete user
                        </button>
                    </form>
                </div>
            <#else >
                <p>Not found</p>
            </#if>
        </div>
    </div>
    </@ui.page>
</#escape>