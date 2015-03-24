<#ftl strip_whitespace=true>
<#import '/ftl/ui.ftl' as ui>
<#-- @ftlvariable name="" type="im.tox.upsourcebot.views.users.AllUsersView" -->
<#escape name as name?html>
    <@ui.page "All users">
    <p><a href="users/new">Create new user</a></p>
    <div class="jumbotron">
        <ul>
            <#list users as user>
                <li><a href="users/${user.id}">${user.name}</a></li>
            </#list>
        </ul>
    </div>
    </@ui.page>
</#escape>