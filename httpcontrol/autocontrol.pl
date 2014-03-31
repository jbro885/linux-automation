#!/usr/bin/perl

use CGI;
use IO::Socket::INET;
use strict;


##################################################
# CONFIGURATION VARIABLES
##################################################

my $AUTOMATED_SERVER_ADDR="localhost";
my $AUTOMATED_SERVER_PORT="1234";


##################################################

my $cgi;
my $request;
my $value;
my $socket;
my $is_connected = 0;

$cgi  = CGI->new;

sub server_connect
{
   if ($is_connected == 1)
   {
      return 1;
   }

   $socket = new IO::Socket::INET (
         PeerHost => $AUTOMATED_SERVER_ADDR,
         PeerPort => $AUTOMATED_SERVER_PORT,
         Proto => 'tcp',
         Timeout => 1
         );
   if (! defined ($socket))
   {
#         print "Cannot connect to $AUTOMATED_SERVER_ADDR:$AUTOMATED_SERVER_PORT\n";
         return 0;
   }
   $is_connected = 1;
#   print "connected to the server\n";
   return 1;
}

sub server_disconnect
{
   if ($is_connected == 1)
   {
      $socket->close();
      return 1;
   }
   return 0;
}

sub send_str
{
   my $str;
   my $size;
   $str = shift;
   server_connect ();

   $size = $socket->send($str);
#   print "have sent $size\n";

# notify server that request has been sent
#   shutdown($socket, 1);
}


sub receive_str
{
   my $response;
   $socket->recv($response, 1024);
   return ($response);
}

sub print_error
{
	print "Content-Type: text/html\n\n";
	print "<HTML><head><title>Test Page</title></head>\n";
	print ("<BODY><H1>Hello World</H1>\n");
	print "<p>Setup is successful.</p></BODY></HTML>";
	exit (1);
}

sub check_request
{
	my $request;
	my $value;
	$request = shift;
	$value = shift;

	if ($request eq "get-global-status")
	{
		return 1;
	}
	elsif ($request eq "set-global-status")
	{
		if ($value eq "on")
		{
			return 1;
		}
		elsif ($value eq "off")
		{
			return 1;
		}
	}
	return 0;
}

sub handle_request
{
	my $request;
	my $value;
   my $tmp;
	$request = shift;
	$value = shift;

   if ($request eq "get-global-status")
   {
      $tmp = "<request cmd=\"get-global-status\"/>\n";
      send_str ($tmp);
      $tmp = receive_str ();
   }

   if ($request eq "set-global-status")
   {
      #here, we already checked the potential values
      #for the $value var. It is either "on" or "off"

      #we can then use this variable when building the
      #string being sent to the daemon
      $tmp = "<request cmd=\"set-global-status\" value=\"$value\"/>\n";
#      print "sending $tmp";
      send_str ($tmp);
      $tmp = receive_str ();
   }

#   print "received: |$tmp|\n";
   print $tmp;
}


##################################################
# MAIN PROGRAM STARTS HERE
##################################################

$request = $cgi->param('request');
$value   = $cgi->param('value');

print "Content-Type: text/plain\n\n";

if (check_request ($request, $value) == 0)
{
	print "<error type=\"invalid-request\"/>\n";
	exit (1);
}

# connect to the server only
# once we are sure the request is legit
if (server_connect () == 0)
{
	print "<error type=\"noserver\"/>\n";
   exit (1);
}


handle_request ($request, $value);

server_disconnect ();

exit (1);

