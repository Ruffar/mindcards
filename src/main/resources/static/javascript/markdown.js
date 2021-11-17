
/*$(".description").load(function(event) {
    console.log("poopoo");
    var thisHtml = $(this).html();
    $(this).html(convertMarkdown(thisHtml));
    $(this).html("poop");
});*/

$(document).ready(function() {
    $(".card-view").map(function(){
        parseCardDescription($(this));
    });
});

function parseCardDescription(cardViewDiv) {
    var description = cardViewDiv.find(".description");
    var unescapedDesc = convertMarkdown(description.text());
    cardViewDiv.find(".unescapedDesc").html(unescapedDesc);
}

function convertMarkdown(text) {

    text = text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;"); //Escape important HTML characters such as &, < and >

    text = parseBold(text);
    text = parseItalic(text);
    text = parseUnderline(text);
    text = parseStrikethrough(text);
    text = parseBlockquote(text);
    text = parseHorizontalRule(text);

    text = parseHyperlink(text);

    text = parseBulletList(text);
    text = parseNumberList(text);

    text = parseLineBreak(text);

    return text;

}

function parseBold(text) {
    return text.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');
}

function parseItalic(text) {
    return text.replace(/\*(.+?)\*/g, '<em>$1</em>');
}

function parseUnderline(text) {
    return text.replace(/__(.+?)__/g, '<u>$1</u>');
}

function parseStrikethrough(text) {
    return text.replace(/~~(.+?)~~/g, '<del>$1</del>');
}

function parseBlockquote(text) {
    return text.replace(/^&gt;(.+?)$/gm, '<blockquote>$1</blockquote>');
}

function parseHorizontalRule(text) {
    return text.replace(/^---$/gm, '<hr/>');
}

function parseHyperlink(text) {
    return text.replace(/\[(.+?)]\((.+?)\)/g, '<a href="$2">$1</a>');
}

function parseBulletList(text) {
    return text.replace(/^-(.+?)$/gm,'<ul><li>$1</li></ul>').replace(/<\/ul>(\s*?)<ul>/g,'');
}

function parseNumberList(text) {
    return text.replace(/^[0-9]+?\.(.+?)$/gm,'<ol><li>$1</li></ol>').replace(/<\/ol>(\s*?)<ol>/g,'');
}

function parseLineBreak(text) {
    return text.replace(/\>\n/g,'>').replace(/\n\</g,'<').replace(/\n/g,'<br/>');
}