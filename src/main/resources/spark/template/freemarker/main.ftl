<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <body>
     ${content}
     <!-- Again, we're serving up the unminified source for clarity. -->
	<script src="js/jquery-2.1.1.js"></script>
	<form id="ajaxForm" action="/query" method="POST">
	Query:<br>
	<input type="text" name="query" value="">
	<br>
	<br><br>
	<input type="submit" value="Submit Query">
	</form>
        
	<script> 
		//http://blog.teamtreehouse.com/create-ajax-contact-form
		var form = $('#ajaxForm');
		$(form).submit(function(event) {
			 event.preventDefault();

			console.log("test");
			 var formData = $(form).serialize();
			$.ajax({
    			 type: 'POST',
   			 url: "/query",
  			 data: formData
			}).done(function(response) {
				$(document.body).append(response+'<br>');
			});
		
		});

	</script>
	
	<#-- <script> console.log('${board}'); -->
	<#-- <script> var board = JSON.parse('${board}') </script> -->
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
