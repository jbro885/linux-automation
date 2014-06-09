#!/usr/bin/perl

use CGI;
use IO::Socket::INET;
use strict;


##################################################
# CONFIGURATION VARIABLES
##################################################
my $conf;
$conf->{'AUTOMATED_ADDR'} = "127.0.0.1";
$conf->{'AUTOMATED_PORT'} = "1234";


##################################################
# CONFIG FILE HANDLING
##################################################

sub read_config 
{  

   return if (! -f "/etc/automated.conf");

   open MYFILE, "/etc/automated.conf";
   while (my $line = <MYFILE>)
   {
      my ($key,$value) = $line =~ /([\w\d\_]+)=([\w\d\.\_]+)/;
      $conf->{$key} = $value;
   }

   close MYFILE;
}  


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
         PeerHost => $conf->{'AUTOMATED_ADDR'},
         PeerPort => $conf->{'AUTOMATED_PORT'},
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

   return 1 if (($request eq "set-global-status") and ($value eq "on"));
   return 1 if (($request eq "set-global-status") and ($value eq "off"));
   return 1 if ($request eq "get-global-status");
   return 1 if ($request eq "get-webcam-picture");
   return 1 if ($request eq "get-summary");
   return 1 if ($request eq "get-events");

   return 0;
}

sub handle_request
{
   my $request;
   my $value;
   my $tmp;
   $request = shift;
   $value = shift;

   if ($request eq "get-summary")
   {
      $tmp = "<request cmd=\"get-summary\"/>\n";
      send_str ($tmp);
      $tmp = receive_str ();

      print "Content-Type: text/plain\n\n";
      print $tmp;
   }


   if ($request eq "get-events")
   {
      $tmp = "<request cmd=\"get-events\"/>\n";
      send_str ($tmp);
      $tmp = receive_str ();

      print "Content-Type: text/plain\n\n";
      print $tmp;
   }



   if ($request eq "get-global-status")
   {
      $tmp = "<request cmd=\"get-global-status\"/>\n";
      send_str ($tmp);
      $tmp = receive_str ();

      print "Content-Type: text/plain\n\n";
      print $tmp;
   }

   if ($request eq "set-global-status")
   {
      $tmp = "<request cmd=\"set-global-status\" value=\"$value\"/>\n";
      send_str ($tmp);
      $tmp = receive_str ();

      print "Content-Type: text/plain\n\n";
      print $tmp;
   }

   if ($request eq "get-webcam-picture")
   {
      $tmp = "<request cmd=\"get-webcam-picture\"/>\n";
      send_str ($tmp);
      print "Content-Type: image/jpeg\n\n";
      while (<$socket>)
      {
         print $_;
      }

   }
}


##################################################
# MAIN PROGRAM STARTS HERE
##################################################

$request = $cgi->param('request');
$value   = $cgi->param('value');


if (check_request ($request, $value) == 0)
{
   print "Content-Type: text/plain\n\n";
   print "<error type=\"invalid-request\"/>\n";
   exit (1);
}

# connect to the server only
# once we are sure the request is legit
if (server_connect () == 0)
{
   print "Content-Type: text/plain\n\n";
   print "<error type=\"noserver\"/>\n";
   exit (1);
}


handle_request ($request, $value);

server_disconnect ();

exit (1);

