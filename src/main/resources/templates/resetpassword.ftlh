<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#if message?? && message == "Password successfully reset"><meta http-equiv="refresh" content="3;URL=/login" /></#if>
    <title>Reset password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet"/>
</head>
<body>
<div class="container fullscreen">
    <div class="vc">
        <form class="fw form-control p-4 text-center" method="post">
            <h2 class="mb-3">Reset password</h2>
            <#if messageType?? && message??>
                <div class="alert alert-${messageType}">${message}</div>
            </#if>
            <#if email??>
                <div class="mb-3">Email: ${email}</div>
            </#if>
            <div class="mb-3">
                <input type="password" name="password" placeholder="New password" class="form-control" required>
            </div>
            <div class="mb-3">
                <input type="password" name="passwordConfirm" placeholder="Confirm new password" class="form-control" required>
            </div>
            <div class="row fsb">
                <button type="submit" class="btn btn-primary mb-2 ps-0 pe-0">RESET PASSWORD</button><br>
            </div>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>