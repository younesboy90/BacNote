// Google AdSense auto ads. Runs only on the public website over http(s):
// not on localhost / file previews, and not inside the iPhone app (bacnote:// scheme).
(function () {
  var host = location.hostname;
  var isPublicSite = /^https?:$/.test(location.protocol) &&
    host !== "" && host !== "localhost" && host !== "127.0.0.1" && !host.endsWith(".local");
  if (!isPublicSite) return;
  var script = document.createElement("script");
  script.async = true;
  script.crossOrigin = "anonymous";
  script.src = "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-5676271357714506";
  document.head.appendChild(script);
})();
