#~/bin/sh

ENABLE_EMAIL=0
SEND=0
TMPFILE=/tmp/automated-motion-sendmail-pic

if [ -x /etc/automated.conf ]; then
	. /etc/automated.conf
fi

#Only send a mail per minute
if [ "$ENABLE_EMAIL" = "1" ]; then
  if [ -f ${TMPFILE} ]; then
    modf_time=`ls -l ${TMPFILE}`
    current_date=`date`
    
    ftime_month=`echo $modf_time | awk '{print $6}'`
    ftime_day=`echo $modf_time | awk '{print $7}'`
    ftime_hrmin=`echo $modf_time | awk '{print $8}'`
    ftime_hr=`echo $ftime_hrmin | cut -d ':' -f1`
    ftime_min=`echo $ftime_hrmin | cut -d ':' -f2`

    current_time=`echo $current_date | awk '{print $4}'`
    ctime_hr=`echo $current_time | cut -d ':' -f1`
    ctime_min=`echo $current_time | cut -d ':' -f2`

    
    if [ $ctime_min -ne $ftime_min ]; then
      SEND=1
    else
      SEND=0
    fi
    
  else
    SEND=1
  fi

   if [ "$SEND" = "1" ]; then
	DATE=`date +%Y%m%d-%H%M`
	ALERT="Motion detected at $DATE check picture for more details and information."
	for addr in $RECIPIENTS; do
		echo "$ALERT" | mail -s "MOTION ALERT on $SYSTEM_NAME" $addr -a $1
	done
	rm -f ${TMPFILE}
	touch ${TMPFILE}
	
   fi
fi

exit 0
