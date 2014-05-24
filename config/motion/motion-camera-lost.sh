#!/bin/sh

ENABLE_EMAIL=0

if [ -x /etc/automated.conf ]; then
	. /etc/automated.conf
fi

if [ "$ENABLE_EMAIL" = "1" ]; then
	DATE=`date +%Y%m%d-%H%M`
	ALERT="Camera was lost at $DATE check app for more details and information."
	for addr in $RECIPIENTS; do
		echo "$ALERT" | mail -s "CAMERA LOST on $SYSTEM_NAME" $addr
	done
fi

exit 0
