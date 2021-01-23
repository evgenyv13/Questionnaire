<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Forgot password?</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet"/>
</head>
<body>
<div class="container fullscreen">
    <div class="vc">
        <form class="fw form-control p-4 text-center" method="post">
            <h2 class="mb-3">Forgot password?</h2>
            <#if messageType?? && message??>
                <div class="alert alert-${messageType}">${message}</div>
            </#if>
            <div class="mb-1">
                <input type="email" name="email" placeholder="Email" class="form-control">
            </div>
            <div class="mb-3">Password reset link will be sent to this email</div>
            <div class="row fsb">
                <button type="submit" class="btn btn-primary mb-2 ps-0 pe-0">SEND LINK</button><br>
            </div>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>