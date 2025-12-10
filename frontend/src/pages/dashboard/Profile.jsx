import React, { useEffect, useState } from 'react';
import {
  Box, Heading, Text, VStack, SimpleGrid, Card, CardBody, Avatar, Divider, Flex, Badge,
  Spinner, Center, useToast, Container, HStack, Grid, GridItem, Icon
} from '@chakra-ui/react';
import { getEmployeeById } from '../../api/employees';
import { Mail, Phone, Calendar, User, Hash, Briefcase, Clock, DollarSign } from 'lucide-react';

const DetailRow = ({ icon, label, value }) => (
  <HStack spacing={4} align="flex-start" py={2}>
    <Center w="32px" h="32px" bg="brand.100" borderRadius="lg" color="brand.600" flexShrink={0}>
      <Icon as={icon} />
    </Center>
    <Box>
      <Text fontSize="xs" color="gray.500" fontWeight="bold" textTransform="uppercase">{label}</Text>
      <Text fontWeight="medium" color="brown.900">{value || "N/A"}</Text>
    </Box>
  </HStack>
);

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const toast = useToast();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const storedUser = localStorage.getItem('user');
        if (!storedUser) {
          throw new Error("No user logged in. Please re-login.");
        }

        const user = JSON.parse(storedUser);
        const employeeId = user.id || user.employeeID;

        if (!employeeId) {
          throw new Error("Invalid user session.");
        }

        // Fetch fresh data from DB
        const data = await getEmployeeById(employeeId);
        setProfile(data);

      } catch (error) {
        toast({
          title: "Error fetching profile",
          description: error.message,
          status: "error",
          duration: 3000,
          isClosable: true,
        });
      } finally {
        setIsLoading(false);
      }
    };

    fetchProfile();
  }, [toast]);

  if (isLoading) {
    return (
      <Center h="50vh">
        <Spinner size="xl" color="brand.500" thickness="4px" />
      </Center>
    );
  }

  if (!profile) {
    return (
      <Center h="50vh">
        <Text color="gray.500">Profile not found. Please log in again.</Text>
      </Center>
    );
  }

  return (
    <Container maxW="container.xl" p={0}>
      <Heading size="lg" color="brown.900" mb={6}>My Profile</Heading>

      <VStack spacing={6} align="stretch" w="full">
        {/* Header with Name */}
        <Box bg="white" p={6} borderRadius="xl" boxShadow="sm" borderLeft="4px solid" borderColor="brand.500">
          <Heading size="md" color="brown.900">Welcome, {profile.name}!</Heading>
        </Box>

        <Card borderRadius="xl" boxShadow="sm">
          <CardBody>
            <Heading size="sm" mb={6} color="brown.800" borderBottom="2px solid" borderColor="brand.100" pb={2} display="inline-block">
              Personal Information
            </Heading>
            <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
              <DetailRow icon={Mail} label="Email Address" value={profile.email} />
              <DetailRow icon={Phone} label="Phone Number" value={profile.contactNo} />
              <DetailRow icon={Calendar} label="Date of Birth" value={profile.dob ? new Date(profile.dob).toLocaleDateString('en-GB', { day: 'numeric', month: 'long', year: 'numeric' }) : 'N/A'} />
              <DetailRow icon={User} label="Gender" value={profile.gender} />
            </SimpleGrid>
          </CardBody>
        </Card>

        <Card borderRadius="xl" boxShadow="sm">
          <CardBody>
            <Heading size="sm" mb={6} color="brown.800" borderBottom="2px solid" borderColor="brand.100" pb={2} display="inline-block">
              Employment Details
            </Heading>
            <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
              <DetailRow icon={Hash} label="Employee ID" value={profile.employeeID} />
              <DetailRow icon={Briefcase} label="Position" value={profile.position?.replace('_', ' ')} />
              <DetailRow icon={Clock} label="Date Joined" value={profile.dateJoined ? new Date(profile.dateJoined).toLocaleDateString('en-GB') : 'N/A'} />
              <DetailRow icon={DollarSign} label="Salary" value={`RM ${profile.salary?.toFixed(2)}`} />
            </SimpleGrid>
          </CardBody>
        </Card>
      </VStack>
    </Container>
  );
};

export default Profile;
