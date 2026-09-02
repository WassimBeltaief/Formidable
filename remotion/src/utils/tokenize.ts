export type Token = { text: string; color: string };

export const TOKEN_COLORS: Record<string, string> = {
  annotation:  "#6F42C1",
  keyword:     "#0033B3",
  type:        "#267F99",
  string:      "#067D17",
  plain:       "#1D1D1D",
  punctuation: "#555555",
};

export function tokenize(line: string): Token[] {
  const rules: [RegExp, string][] = [
    [/^(@\w+)/,                                                           "annotation"],
    [/^(data class|class|val|fun|override|suspend|return)\b/,             "keyword"],
    [/^(String|Boolean|Int|Unit|Map|Any|ContactMethod|LoginForm)\b/,      "type"],
    [/^"[^"]*"/,                                                          "string"],
    [/^[(){}:=,<>?*[\].]/,                                                "punctuation"],
    [/^\s+/,                                                              "plain"],
    [/^\w+/,                                                              "plain"],
  ];
  const tokens: Token[] = [];
  let rest = line;
  while (rest.length > 0) {
    let matched = false;
    for (const [re, color] of rules) {
      const m = rest.match(re);
      if (m) {
        tokens.push({ text: m[0], color: TOKEN_COLORS[color] });
        rest = rest.slice(m[0].length);
        matched = true;
        break;
      }
    }
    if (!matched) {
      tokens.push({ text: rest[0], color: TOKEN_COLORS.plain });
      rest = rest.slice(1);
    }
  }
  return tokens;
}
