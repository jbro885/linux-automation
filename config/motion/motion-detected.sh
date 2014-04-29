#~/bin/sh

ENABLE_EMAIL=0

if [ -x /etc/automated.conf ]; then
	. /etc/automated.conf
fi

if [ "$ENABLE_EMAIL" = "1" ]; then
	DATE=`date +%Y%m%d-%H%M`
	ALERT="Motion detected at $DATE check picture for more details and information."
	for addr in $RECIPIENTS; do
		echo "$ALERT" | mail -s "MOTION ALERT on $SYSTEM_NAME" $addr
	done
fi

exit 0
