tests:
	nc localhost 1234 < tests/request-global-status.xml
	nc localhost 1234 < tests/request-global-status-set-on.xml
	nc localhost 1234 < tests/request-global-status-set-off.xml

launch:
	./src/automated.pl

.PHONY: tests