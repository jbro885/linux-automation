#!/usr/bin/perl -w

use Data::Dumper;
use IO::Socket;
use IO::Select;
use Test::XML;
use XML::Simple qw(:strict);


my $CLIENT_SOCKET = 0;
my $readable_handles;
my $status;
my $buf;

my $conf;
$conf->{'MOTION_STREAM_ADDR'} = "127.0.0.1";
$conf->{'MOTION_STREAM_PORT'} = 8081;
$conf->{'SERVER_ADDR'}        = "127.0.0.1";
$conf->{'SERVER_PORT'}        = 1234;
$conf->{'MOTION_INIT_SCRIPT'} = "/etc/init.d/motion";


sub read_config
{

   return if (! -f "/etc/automated.conf");

   open MYFILE, "/etc/automated.conf";
   while (my $line = <MYFILE>)
   {
      my ($key,$value) = $line =~ /([\w\d\_]+)=([\w\d\.\_]+)/;

      if (defined ($key) && defined ($value))
      {
         $conf->{$key} = $value;
      }
   }

   close MYFILE;
}

sub start_global
{
   print "Starting the system globally\n";

   #starting motion
   if ( -x $conf->{'MOTION_INIT_SCRIPT'})
   {
      system ($conf->{'MOTION_INIT_SCRIPT'} . " start");
   }
   return 1;
}

sub stop_global
{
   print "Stopping the system globally\n";


   #stopping motion
   if ( -x $conf->{'MOTION_INIT_SCRIPT'})
   {
      system ($conf->{'MOTION_INIT_SCRIPT'} . " stop");
   }

   print "System stopped\n";
   return 1;
}

sub init
{
   $status->{'status'} = "on";
   start_global ();
}


sub is_valid_request
{
   my $buf = shift;
   if ( is_well_formed_xml ($buf))
   {
      return 1;
   }
   return 0;
}

sub get_webcam_picture
{
   use LWP::UserAgent;
   my $ua = LWP::UserAgent->new;
   $ua->agent("MyApp/0.1 ");
   $ua->timeout (1);

   my $url = "http://" . $conf->{'MOTION_STREAM_ADDR'}.":".$conf->{'MOTION_STREAM_PORT'};

   my $res = $ua->get($url);

   my $content = $res->content;
   my ($size) = $content =~ /Content-Length:\s*(\d+)/;
   if (! defined ($size))
   {
      print "size is not defined\n";
      return "";
   }
   my $length = length ($content);
   my $img = substr ($content, $length - $size - 2, $size);
   return $img;
}

sub build_answer
{
   my $request = shift; 
   my $answer;

#By default, we assume the answer type
#will be quit. This ensures to define
#at least the type.
   $answer->{'type'} = "quit";

   if ($request->{'cmd'} eq "get-global-status")
   {
      $answer->{'type'} = "global-status";
      $answer->{'value'} = $status->{'status'};
   }
   elsif ($request->{'cmd'} eq "set-global-status")
   {

      $answer->{'type'} = "global-status";
      if ($request->{'value'} eq "on")
      {

         if (start_global () == 1)
         {      
            $answer->{'value'} = "on";
            $status->{'status'} = "off";
         }

      }
      else
      {
         if (stop_global () == 1)
         {
            $answer->{'value'} = "off";
            $status->{'status'} = "off";
         }
      }
   }
   return $answer;
}



#############################################
# MAIN PROGRAM
############################################
init();
read_config();

my $server_socket = new IO::Socket::INET (
      LocalHost => $conf->{'SERVER_ADDR'},
      LocalPort => $conf->{'SERVER_PORT'},
      Proto => 'tcp',
      Listen => 1235,
      Reuse => 1,
      timeout => 5,
      );
die "Could not create socket: $!n" unless $server_socket;

#print "created socket $server_socket";

$Read_Handles_Object = new IO::Select(); # create handle set for reading
$Read_Handles_Object->add($server_socket); # add the main socket to the set

while (1) 
{ # forever

   $rh = $server_socket->accept;
   while (defined ($rh) && defined (fileno ($rh)) && (fileno ($rh) != -1 ) && ($buf = <$rh>))
   {
      if ((defined ($buf)) && (length ($buf) > 0 ) )
      {
         if (is_valid_request ($buf) != 1)
         {
            $Read_Handles_Object->remove($rh);
            close($rh);
         }
         else
         {
            my $request = XMLin($buf,KeyAttr => { server => 'name' }, ForceArray => [ 'server', 'address' ]);

#here, we have either a bad answer or a request to quit
            if ($request->{'cmd'} eq "get-webcam-picture")
            {
               print $rh get_webcam_picture();
               $rh->flush;

#                        open MYFILE , "/tmp/pic.png";
#                        while (<MYFILE>)
#                        {
#                           print $rh $_;
#                           $rh->flush;
#                        }
#                        close MYFILE;

            }
            else
            {
               my $answer = build_answer ($request);
               if ( (! defined ($answer)) || (! defined ($answer->{'type'})) || ($answer->{'type'} eq "quit"))
               {
                  print "";
               }
               else
               {
                  my $output = XMLout ($answer,KeyAttr => { server => 'answer' }) . "\n";
                  print $rh $output;

                  $rh->flush;

               }
            }
         }
      }
      close($rh);
   }

}
