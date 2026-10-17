import SwiftUI
import UIKit
import WebKit

@main
struct BACNoteCalculeApp: App {
    var body: some Scene {
        WindowGroup {
            CalculatorWebView()
        }
    }
}

private struct CalculatorWebView: UIViewRepresentable {
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> WKWebView {
        let configuration = WKWebViewConfiguration()
        configuration.setURLSchemeHandler(context.coordinator.websiteHandler, forURLScheme: "bacnote")

        let webView = WKWebView(frame: .zero, configuration: configuration)
        webView.navigationDelegate = context.coordinator
        webView.uiDelegate = context.coordinator
        webView.allowsBackForwardNavigationGestures = true
        webView.backgroundColor = UIColor(
            red: CGFloat(247) / 255,
            green: CGFloat(248) / 255,
            blue: CGFloat(250) / 255,
            alpha: 1
        )

        guard let startURL = URL(string: "bacnote://app/index.html") else {
            preconditionFailure("The calculator start URL is invalid.")
        }
        webView.load(URLRequest(url: startURL))
        return webView
    }

    func updateUIView(_ webView: WKWebView, context: Context) {}

    final class Coordinator: NSObject, WKNavigationDelegate, WKUIDelegate {
        let websiteHandler: LocalWebsiteHandler

        override init() {
            guard let webRoot = Bundle.main.resourceURL?.appendingPathComponent("WebContent", isDirectory: true) else {
                preconditionFailure("The bundled calculator website is missing.")
            }
            websiteHandler = LocalWebsiteHandler(rootURL: webRoot)
            super.init()
        }

        func webView(
            _ webView: WKWebView,
            decidePolicyFor navigationAction: WKNavigationAction,
            decisionHandler: @escaping (WKNavigationActionPolicy) -> Void
        ) {
            guard
                navigationAction.navigationType == .linkActivated,
                let url = navigationAction.request.url,
                let scheme = url.scheme?.lowercased(),
                scheme == "http" || scheme == "https"
            else {
                decisionHandler(.allow)
                return
            }

            UIApplication.shared.open(url)
            decisionHandler(.cancel)
        }

        func webView(
            _ webView: WKWebView,
            createWebViewWith configuration: WKWebViewConfiguration,
            for navigationAction: WKNavigationAction,
            windowFeatures: WKWindowFeatures
        ) -> WKWebView? {
            guard
                let url = navigationAction.request.url,
                let scheme = url.scheme?.lowercased(),
                scheme == "http" || scheme == "https"
            else {
                return nil
            }

            UIApplication.shared.open(url)
            return nil
        }
    }
}

private final class LocalWebsiteHandler: NSObject, WKURLSchemeHandler {
    private let rootURL: URL

    init(rootURL: URL) {
        self.rootURL = rootURL.standardizedFileURL
    }

    func webView(_ webView: WKWebView, start urlSchemeTask: WKURLSchemeTask) {
        guard
            let url = urlSchemeTask.request.url,
            url.scheme?.lowercased() == "bacnote",
            url.host?.lowercased() == "app",
            let encodedPath = URLComponents(url: url, resolvingAgainstBaseURL: false)?.percentEncodedPath,
            let path = encodedPath.removingPercentEncoding
        else {
            urlSchemeTask.didFailWithError(URLError(.badURL))
            return
        }

        let components = path.split(separator: "/", omittingEmptySubsequences: true)
        guard components.allSatisfy({ $0 != "." && $0 != ".." }) else {
            urlSchemeTask.didFailWithError(URLError(.badURL))
            return
        }

        let relativePath = components.isEmpty ? "index.html" : components.joined(separator: "/")
        let fileURL = rootURL.appendingPathComponent(relativePath).standardizedFileURL
        guard fileURL.path.hasPrefix(rootURL.path + "/") else {
            urlSchemeTask.didFailWithError(URLError(.badURL))
            return
        }

        do {
            let data = try Data(contentsOf: fileURL)
            let response = URLResponse(
                url: url,
                mimeType: Self.mimeType(for: fileURL.pathExtension),
                expectedContentLength: data.count,
                textEncodingName: nil
            )
            urlSchemeTask.didReceive(response)
            urlSchemeTask.didReceive(data)
            urlSchemeTask.didFinish()
        } catch {
            urlSchemeTask.didFailWithError(error)
        }
    }

    func webView(_ webView: WKWebView, stop urlSchemeTask: WKURLSchemeTask) {}

    private static func mimeType(for fileExtension: String) -> String {
        switch fileExtension.lowercased() {
        case "html": return "text/html"
        case "css": return "text/css"
        case "js": return "text/javascript"
        case "png": return "image/png"
        case "svg": return "image/svg+xml"
        case "ico": return "image/x-icon"
        default: return "application/octet-stream"
        }
    }
}
