
/*$(".description").load(function(event) {
    console.log("poopoo");
    var thisHtml = $(this).html();
    $(this).html(convertMarkdown(thisHtml));
    $(this).html("poop");
});*/

//Stack
class Stack {

    constructor() {
        this.items = [];
        this.top = -1;
    }

    isEmpty() {
        return this.top < 0;
    }

    push(item) {
        this.top += 1;
        this.items[this.top] = item;
    }

    peek() {
        if (!this.isEmpty()) {
            return this.items[this.top];
        }
        return null;
    }

    pop() {
        if (!this.isEmpty()) {
            this.top -= 1;
            return this.items[this.top+1];
        }
        return null;
    }

}

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

    text = parseMathExpression(text);

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

function parseMathExpression(text) {
    return text.replace(/\\\((.+?)\\\)/g,(match,$1)=> {
        var expression = match.slice(2,match.length-2);
        var symbolStack = new Stack();
        var isVariable = false;
        var output = "<span class='mathExp'>"
        for (let i = 0; i < expression.length; i++) {
            currentChar = expression[i];
            if (currentChar == '\\') {
                if (expression.slice(i+1,i+5) == "frac") {
                    output += "<div class='frac'><div class='fracExp'>";
                    symbolStack.push("frac");
                    i += 4;
                } else if (expression.slice(i+1,i+5) == "sqrt") {
                    output += "<span>&#x221A;</span><span><hr style='padding-top: 0.1em; padding-bottom: 0.1em; margin-top: 0; margin-bottom: 0'>";
                    symbolStack.push("sqrt");
                    i+=4;
                } else if (expression.slice(i+1,i+4) == "sin") {
                    output += "<span style='font-style: italic;'>sin</span><span style='padding-left: 0.25em; padding-right:0.25em;'>";
                    symbolStack.push("sin");
                    i += 3;
                }
            } else if (currentChar == '{') {
                symbolStack.push("{");
            } else {
                if (currentChar == '}' && symbolStack.peek() == '{') {
                    symbolStack.pop();
                } else if (currentChar.match(/[a-z]/i)) { //i means not case sensitive
                    output += "<span class='variables'>"+currentChar;
                    isVariable = true;
                } else if (isVariable) {
                    output += currentChar+"</span>";
                    isVariable = false;
                } else {
                    output += currentChar;
                }
                if (!symbolStack.isEmpty()) {
                    var symbol = symbolStack.peek();
                    switch(symbol) {
                        case "frac":
                            output += "</div><hr class='fracLine'><div class='fracExp'>";
                            symbolStack.pop();
                            symbolStack.push("frac1");
                            break;
                        case "frac1":
                            output += "</div></div>";
                            symbolStack.pop();
                            break;
                        case "sqrt":
                            output += "</span>";
                            symbolStack.pop();
                            break;
                        case "sin":
                            output += "</span>";
                            symbolStack.pop();
                            break;
                    }
                }
            }
        }
        return output+"</span>";
    });
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