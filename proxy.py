#!/bin/env python3

from http.server import BaseHTTPRequestHandler, HTTPServer
import requests

PORT = 9000

class Proxy(BaseHTTPRequestHandler):
    def _set_response(self):
        self.send_response(200)
        self.end_headers()

    def do_POST(self):
        self._make_request()

    def _make_request(self):
        self._set_response()
        headers = self.headers

        length = int(headers["Content-Length"])

        content = self.rfile.read(length)
        headers["Content-Type"] = "text/xml; charset=utf-8"
        request = requests.post(self.path, headers=headers, data=content)
        self.wfile.write(request.content)

server_address = ('127.0.0.1', PORT)
server = HTTPServer(server_address, Proxy)

try:
    server.serve_forever()
except KeyboardInterrupt:
    pass

server.server_close()
