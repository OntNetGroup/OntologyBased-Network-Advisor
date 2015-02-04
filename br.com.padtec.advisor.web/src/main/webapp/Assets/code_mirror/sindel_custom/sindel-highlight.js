// LUA mode. Ported to CodeMirror 2 from Franciszek Wawrzak's
// CodeMirror 1 mode.
// highlights keywords, strings, comments (no leveling supported! ("[==[")), tokens, basic indenting

CodeMirror.defineMode("sindel", function(config, parserConfig) {
  var indentUnit = config.indentUnit;

  function prefixRE(words) {
    return new RegExp("^(?:" + words.join("|") + ")", "i");
  }
  function wordRE(words) {
    return new RegExp("^(?:" + words.join("|") + ")$", "i");
  }
  var specials = wordRE(parserConfig.specials || []);

  // long list of standard functions from lua manual
  var builtins = wordRE([	
	"tf",
	"so-tf",
	"sk-tf",
	"bi-tf",
	"af",
	"so-af",
	"sk-af",
	"bi-af",
	"lpf",
	"so-lpf",
	"sk-lpf",
	"bi-lpf",
	"matrix",
	"uni-matrix",
	"so-matrix",
	"sk-matrix",
	"bi-matrix",
	"sn",
	"pm",
	"input",
	"output",
	"rp",
	"fep",
	"ap",
	"fp",
	"ate",
	"nc",
	"lc",
	"mc",
	"cfte",
	"layer",
	"equip",
	"in-int",
	"out-int",
	"site"

,	"location",
	"type"
  ]);
  var keywords = wordRE([
	"binds",
	"connects",
	"client",
	"maps"
  ]);

  var indentTokens = wordRE(["\\(", "{"]);
  var dedentTokens = wordRE(["\\)", "}"]);
  var dedentPartial = prefixRE(["\\)", "}"]);

  function readBracket(stream) {
    var level = 0;
    while (stream.eat("=")) ++level;
    stream.eat("[");
    return level;
  }

  function normal(stream, state) {
    var ch = stream.next();
    if (ch == "/" && stream.eat("*")) {
        return (state.cur = bracketed(readBracket(stream), "comment"))(stream, state);
      stream.skipToEnd();
      return "comment";
    }
    if (ch == "\"" || ch == "'")
      return (state.cur = string(ch))(stream, state);
    //if (ch == "[" && /[\[=]/.test(stream.peek()))
     // return (state.cur = bracketed(readBracket(stream), "string"))(stream, state);
    if (/\d/.test(ch)) {
      stream.eatWhile(/[\w.%]/);
      return "number";
    }
    if (/[\w_]/.test(ch)) {
      stream.eatWhile(/[\w\\\-_.]/);
      return "variable";
    }
    return null;
  }

  function bracketed(level, style) {
    return function(stream, state) {
      var curlev = null, ch;
      while ((ch = stream.next()) != null) {
        if (curlev == null) {if (ch == "*") curlev = 0;}
        else if (ch == "=") ++curlev;
        else if (ch == "/" && curlev == level) { state.cur = normal; break; }
        else curlev = null;
      }
      return style;
    };
  }

  function string(quote) {
    return function(stream, state) {
      var escaped = false, ch;
      while ((ch = stream.next()) != null) {
        if (ch == quote && !escaped) break;
        escaped = !escaped && ch == "\\";
      }
      if (!escaped) state.cur = normal;
      return "string";
    };
  }

  return {
    startState: function(basecol) {
      return {basecol: basecol || 0, indentDepth: 0, cur: normal};
    },

    token: function(stream, state) {
      if (stream.eatSpace()) return null;
      var style = state.cur(stream, state);
      var word = stream.current();
      if (style == "variable") {
        if (keywords.test(word)) style = "keyword";
        else if (builtins.test(word)) style = "builtin";
        else if (specials.test(word)) style = "variable-2";
      }
      if ((style != "comment") && (style != "string")){
        if (indentTokens.test(word)) ++state.indentDepth;
        else if (dedentTokens.test(word)) --state.indentDepth;
      }
      return style;
    },

    indent: function(state, textAfter) {
      var closing = dedentPartial.test(textAfter);
      return state.basecol + indentUnit * (state.indentDepth - (closing ? 1 : 0));
    },

    blockCommentStart: "/*",
    blockCommentEnd: "*/"
  };
});

CodeMirror.defineMIME("text/sindel", "sindel");
