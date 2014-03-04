#!/usr/bin/perl -w

use Data::Dumper;
use IO::Socket;
use IO::Select;
use Test::XML;
use XML::Simple qw(:strict);


my $CLIENT_SOCKET = 0;
my $readable_handles;
my $status;

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
  
  if ($request->{'cmd'} eq "get-global-status")
  {
    $answer->{'type'} = "global-status";
    $answer->{'value'} = $status->{'status'};
  }
  else
  {
    $answer->{'type'} = "quit";
  }
  return $answer;
}


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
  { # keep in this loop as long as there is a readable filehandle..
  # get the set of readable handles
  #
    ($readable_handles) = IO::Select->select($Read_Handles_Object, undef, undef, 100);

#    if (! defined ($readable_handles))
#    {
#      if (! scalar @$readable_handles)
#      {
#	print "nothing left to read...n";
#	last
#      }
 #   }
    foreach $rh (@{$readable_handles}) 
    {
    #print "read handle found : '$rh'n";
    # if it is the main socket then we have an incoming connection and
    # we should accept() it and then add the new socket to the $Read_Handles_Object
      if ($rh == $server_socket) 
      {
      # you get this when a new socket connection is formed..a new client starts and it assigns
      # a socket filehandle to it.
      #print " accept();
	accept ($client, $server_socket);
	$Read_Handles_Object->add($client);
	$ClientNumber{$client}=++$CLIENT_COUNT;
	#print " %%% Adding '$client' to read setn";
      }
# otherwise it is an ordinary socket and we should read and process the request
      else 
      {
#print " --this is an ordinary socket '$rh'n";

	$buf = $rh->getline(); # grab a line .. it shouldnt block due to the select
	if (! defined $buf) {
	  # the other end of the socket closed...close our end and remove
	  # it from the list of sockets to listen to..
	  print " |FROM CLIENT ".$ClientNumber{$rh}."| --socket closed--n";
	  $Read_Handles_Object->remove($rh);
	  
	  $CLIENT_COUNT = $CLIENT_COUNT - 1;
	  close($rh);
	}
	else
	{
	  if (is_valid_request ($buf) != 1)
	  {
	    print "bad request, closing\n";
	    $Read_Handles_Object->remove($rh);
	  
	    $CLIENT_COUNT = $CLIENT_COUNT - 1;
	    close($rh);
	  }
	  else
	  {
	    my $request = XMLin($buf,KeyAttr => { server => 'name' }, ForceArray => [ 'server', 'address' ]);
	    print Dumper $request;
	    my $answer = build_answer ($request);
	    
	    if ($answer->{'type'} eq "quit")
	    {
	      print "bad answer, closing\n";
	    
	      $Read_Handles_Object->remove($rh);
	  
	      $CLIENT_COUNT = $CLIENT_COUNT - 1;
	      close($rh);
	    }
	    print " |FROM CLIENT ".$ClientNumber{$rh}."|$buf";
	    my $output = XMLout ($answer,KeyAttr => { server => 'answer' }) . "\n";
	    print $rh $output;
	    print Dumper $answer;
	    print " |TO CLIENT ".$ClientNumber{$rh}."|$output";

	    $rh->flush;
	    
	  }
	}
      }
    }
  }


# after going through the sockets I'll get back to some other work
# .. I'll simulate that with a sleep 3 ..


}
