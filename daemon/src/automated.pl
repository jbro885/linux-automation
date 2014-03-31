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
sub start_global
{
   print "Starting the system globally\n";
   return 1;
}

sub stop_global
{
   print "Stopping the system globally\n";
   return 1;
}

sub init
{
   $status->{'status'} = "on";
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
      if ($request->{'value'} eq "on")
      {
         $answer->{'type'} = "global-status";

         if (start_global () == 1)
         {      
            $answer->{'value'} = "on";
         }

      }
      else
      {
         if (stop_global () == 1)
         {
            $answer->{'value'} = "off";
         }
      }
   }
   return $answer;
}



#############################################
# MAIN PROGRAM
############################################
init();

my $server_socket = new IO::Socket::INET (
      LocalHost => "localhost",
      LocalPort => 1234,
      Proto => 'tcp',
      Listen => 1235,
      Reuse => 1,
      timeout => 5,
      );
die "Could not create socket: $!n" unless $server_socket;

#print "created socket $server_socket";

$Read_Handles_Object = new IO::Select(); # create handle set for reading
$Read_Handles_Object->add($server_socket); # add the main socket to the set

while (1) { # forever


   while (1) 
   { 
      ($readable_handles) = IO::Select->select($Read_Handles_Object, undef, undef, 100);

      foreach $rh (@{$readable_handles}) 
      {
         if ($rh == $server_socket) 
         {
            accept ($client, $server_socket);
            $Read_Handles_Object->add($client);
         }
         else 
         {
            while (defined ($rh) && defined (fileno ($rh)) && (fileno ($rh) != -1 ) && ($buf = <$rh>))
            {
               if (! defined $buf) 
               {
                  $Read_Handles_Object->remove($rh);
                  close($rh);
               }
               else
               {
                  if (is_valid_request ($buf) != 1)
                  {
                     $Read_Handles_Object->remove($rh);
                     close($rh);
                  }
                  else
                  {
                     my $request = XMLin($buf,KeyAttr => { server => 'name' }, ForceArray => [ 'server', 'address' ]);
                     my $answer = build_answer ($request);

                     if ( (! defined ($answer)) || (! defined ($answer->{'type'})) || ($answer->{'type'} eq "quit"))
                     {
                        $Read_Handles_Object->remove($rh);
                        close($rh);
                     }
                     else
                     {
                        my $output = XMLout ($answer,KeyAttr => { server => 'answer' }) . "\n";
                        print $rh $output;

                        $rh->flush;

                        $Read_Handles_Object->remove($rh);
                        close($rh);
                     }
                  }
               }
            }
         }
      }
   }
}
