#!/usr/bin/perl

use CGI;
use strict;

my $cgi;
my $request;
my $value;

$cgi  = CGI->new;

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
	elsif ($request eq "get-global-status")
	{
		return 1;
	}
	return 0;
}


$request = $cgi->param('request');
$value   = $cgi->param('value');

print "Content-Type: text/plain\n\n";

if (check_request ($request, $value) == 0)
{
	print "invalid request\n";
	exit (1);
}
print "Request=" . $request ."\n";
print "Value=" . $value ."\n";

exit (1);

