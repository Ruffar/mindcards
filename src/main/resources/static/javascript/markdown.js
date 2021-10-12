
$(".description").load(function(event) {
    console.log("poopoo");
    var thisHtml = $(this).html();
    $(this).html(convertMarkdown(thisHtml));
    $(this).html("poop");
});

function convertMarkdown(text) {

    text.replace(/\*\*(.*?)\*\*/gi, '<em>$1</em>');
    text.replace(/\*(.*?)\*/gi, '<em>$1</em>');

    return text;

}