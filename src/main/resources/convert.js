var page = require( "webpage" ).create();
var html = consoleRead();

//Log HTML
//console.log(html)

//Set HTML
page.setContent(html, "4o4.me");
var contents = page.renderBase64( "png" );
console.log(contents)

//Exit
phantom.exit();

function consoleRead() {
    var system = require('system');
    var line = system.stdin.readLine();
   
    return line;
}