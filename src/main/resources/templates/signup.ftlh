<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign Up</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet"/>
</head>
<body>
<div class="container fullscreen">
    <div class="mt-5 vc">
        <form class="fw form-control p-4 text-center" method="post">
            <h2 class="mb-3 logo">Questionnaire</h2>
            <h2 class="mb-3">Sign Up</h2>
            <#if messageType?? && message??>
                <div class="alert alert-${messageType}" role="alert">${message}</div>
            </#if>
            <div class="mb-3">
                <input type="email" name="email" placeholder="Email" class="form-control" required/>
            </div>
            <div class="mb-3">
                <input type="password" name="password" placeholder="Password" class="form-control" required/>
            </div>
            <div class="mb-3">
                <input type="password" name="passwordConfirm" placeholder="Confirm password" class="form-control" required/>
            </div>
            <div class="mb-3">
                <input placeholder="First name" name="firstName" pattern="^([А-Я]{1}[а-яё]{1,23}|[A-Z]{1}[a-z]{1,23})$" class="form-control"/>
            </div>
            <div class="mb-3">
                <input placeholder="Last name" name="lastName" pattern="^([А-Я]{1}[а-яё]{1,23}|[A-Z]{1}[a-z]{1,23})$" class="form-control"/>
            </div>
            <div class="mb-3">
                <input placeholder="Phone number" name="phoneNumber" pattern="[0-9+][0-9() ]*" class="form-control"/>
            </div>
            <div class="row fsb">
                <button type="submit" class="btn btn-primary mb-2 ps-0 pe-0">SIGN UP</button><br>
            </div>
            Already have account?
            <a href="/login">Log in</a>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>