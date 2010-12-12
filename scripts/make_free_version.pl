#!/usr/bin/perl -w
use strict;

my $dir = "/Users/david/Documents/Projects/Repositories/checkin4me/CheckIn4me";

if (-e "$dir/AndroidManifest.xml")
{
	system("rm $dir/AndroidManifest.xml");
}

system("ln -s $dir/FreeManifest.xml $dir/AndroidManifest.xml");
