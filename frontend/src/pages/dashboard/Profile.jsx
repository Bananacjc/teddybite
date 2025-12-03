import React from 'react';
import { Box, Heading, Text, VStack, SimpleGrid, Card, CardBody, Avatar, Divider, Flex, Badge } from '@chakra-ui/react';

const Profile = () => {
  return (
    <VStack spacing={8} align="stretch">
      <Heading size="lg" color="brown.900">My Profile</Heading>
      
      <Card borderRadius="2xl" boxShadow="sm">
        <CardBody>
          <Flex direction={{ base: 'column', md: 'row' }} align="center" gap={8}>
            <Avatar size="2xl" name="John Doe" bg="brand.500" />
            <VStack align="start" spacing={2} flex={1}>
              <Heading size="md">John Doe</Heading>
              <Text color="gray.500">Senior Barista</Text>
              <HStack spacing={2}>
                <Badge colorScheme="green">Active</Badge>
                <Badge colorScheme="purple">Full Time</Badge>
              </HStack>
            </VStack>
          </Flex>
        </CardBody>
      </Card>

      <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
        <Card borderRadius="xl" boxShadow="sm">
          <CardBody>
            <Heading size="sm" mb={4}>Personal Information</Heading>
            <VStack align="stretch" spacing={3}>
              <Box>
                <Text fontSize="xs" color="gray.500">Email Address</Text>
                <Text fontWeight="medium">john.doe@teddybite.com</Text>
              </Box>
              <Divider />
              <Box>
                <Text fontSize="xs" color="gray.500">Phone Number</Text>
                <Text fontWeight="medium">+60 12-345 6789</Text>
              </Box>
              <Divider />
              <Box>
                <Text fontSize="xs" color="gray.500">Address</Text>
                <Text fontWeight="medium">123, Jalan Teddy, Kuala Lumpur</Text>
              </Box>
            </VStack>
          </CardBody>
        </Card>

        <Card borderRadius="xl" boxShadow="sm">
          <CardBody>
            <Heading size="sm" mb={4}>Work Information</Heading>
            <VStack align="stretch" spacing={3}>
              <Box>
                <Text fontSize="xs" color="gray.500">Employee ID</Text>
                <Text fontWeight="medium">EMP-2024-001</Text>
              </Box>
              <Divider />
              <Box>
                <Text fontSize="xs" color="gray.500">Date Joined</Text>
                <Text fontWeight="medium">15 Jan 2024</Text>
              </Box>
              <Divider />
              <Box>
                <Text fontSize="xs" color="gray.500">Department</Text>
                <Text fontWeight="medium">Kitchen / Service</Text>
              </Box>
            </VStack>
          </CardBody>
        </Card>
      </SimpleGrid>
    </VStack>
  );
};

import { HStack } from '@chakra-ui/react'; // Forgot to import HStack
export default Profile;
